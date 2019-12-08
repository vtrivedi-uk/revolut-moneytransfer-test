package io.revolut.moneytransfer.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import io.revolut.moneytransfer.domain.AccountEntity;
import io.revolut.moneytransfer.domain.MoneyTransferEntity;
import io.revolut.moneytransfer.domain.MoneyTransferStatus;
import io.revolut.moneytransfer.domain.ApiResponseBean;
import io.revolut.moneytransfer.repository.AccountRepository;
import io.revolut.moneytransfer.repository.MoneyTransferRepository;
import io.revolut.moneytransfer.util.JsonHelper;

public class AccountService {

    private final AccountRepository accountEntityRepository;
    private final MoneyTransferRepository moneyTransferRepository;

    @Inject
    AccountService(AccountRepository accountEntityRepository, MoneyTransferRepository moneyTransferRepository) {
        this.accountEntityRepository = accountEntityRepository;
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public ApiResponseBean createAccount(AccountEntity accountEntity) {
        Set<ConstraintViolation<AccountEntity>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(accountEntity);
        if (constraintViolations.size() == 0) {
            this.accountEntityRepository.saveAccount(accountEntity);
            return ApiResponseBean.getSuccessBean(201);
        }
        return ApiResponseBean.getErrorBean(400, constraintViolations.iterator().next().getMessage());
    }

    public ApiResponseBean createAccountTransaction(String originatorAccountNumber, MoneyTransferEntity moneyTransfer) throws Exception {
        Set<ConstraintViolation<MoneyTransferEntity>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(moneyTransfer);
        moneyTransfer.setOriginatorAccountNumber(Long.valueOf(originatorAccountNumber));
        if (constraintViolations.size() == 0) {
            Long beneficiaryAccountNumber = moneyTransfer.getBeneficiaryAccountNumber();
            //check if both accounts exist in database.
            if (accountEntityRepository.checkIfAccountExists(Long.valueOf(originatorAccountNumber)) && accountEntityRepository.checkIfAccountExists(beneficiaryAccountNumber)) {
            	// check if there is sufficient balance to make this transfer
                if (checkForSufficientBalance(moneyTransfer)) {
                    try {
                        initiateMoneyTransfer(moneyTransfer);
                    } catch (Exception ex) {
                        return ApiResponseBean.getErrorBean(500, "Could not complete request");
                    }
                    //now save the transaction entity
                    moneyTransferRepository.transferMoney(moneyTransfer, MoneyTransferStatus.SUCCESS, "");
                    return ApiResponseBean.getSuccessBean(201);
                }
                String reasonForFailure = "Insufficient Funds to complete transaction";
                moneyTransferRepository.transferMoney(moneyTransfer, MoneyTransferStatus.FAILED, reasonForFailure);
                return ApiResponseBean.getErrorBean(403, reasonForFailure);
            }
            return ApiResponseBean.getErrorBean(404, "Account with id = " + originatorAccountNumber + " does not exist.");
        }
        String errorMessage = constraintViolations.iterator().next().getMessage();
        return ApiResponseBean.getErrorBean(400, errorMessage);
    }

    public ApiResponseBean getAllAccounts() {
        List<AccountEntity> accountEntities = this.accountEntityRepository.getAllAccounts();
        if (accountEntities.isEmpty())
            return ApiResponseBean.getSuccessBean(200, "[]");
        return ApiResponseBean.getSuccessBean(200, JsonHelper.unmarshal(accountEntities));
    }

    public ApiResponseBean getAccountById(String id) {
        AccountEntity accountEntity = this.accountEntityRepository.getAccountById(Long.valueOf(id));
        if (Objects.nonNull(accountEntity))
            return ApiResponseBean.getSuccessBean(200, JsonHelper.unmarshal(accountEntity));
        return ApiResponseBean.getErrorBean(404, "Account with id=" + id + " not found.");
    }

    public ApiResponseBean deleteAccountById(String id) {
        if (Objects.nonNull(accountEntityRepository.getAccountById(Long.valueOf(id)))) {
            accountEntityRepository.deleteAccount(Long.valueOf(id));
            return ApiResponseBean.getSuccessBean(204);
        }
        return ApiResponseBean.getErrorBean(404, "Account with id =" + id + " not found.");
    }

    private boolean checkForSufficientBalance(MoneyTransferEntity accountTransactionEntity) {
        AccountEntity originator =  accountEntityRepository.getAccountById(accountTransactionEntity.getOriginatorAccountNumber());
        BigDecimal transactionAmount = accountTransactionEntity.getTransferAmount();
        return originator.getAccountBalance().compareTo(transactionAmount) >= 0;
    }

    void initiateMoneyTransfer(MoneyTransferEntity moneyTransfer) {
        AccountEntity originator = accountEntityRepository.getAccountById(moneyTransfer.getOriginatorAccountNumber());
        AccountEntity beneficiary = accountEntityRepository.getAccountById(moneyTransfer.getBeneficiaryAccountNumber());
        BigDecimal transferAmount = moneyTransfer.getTransferAmount();
        AccountEntity updatedOriginatorAccountBalance = debitAccountEntity(originator, transferAmount);
        AccountEntity updatedBeneficiaryAccountBalance = creditAccountEntity(beneficiary, transferAmount);
        accountEntityRepository.processMoneyTransfer(updatedOriginatorAccountBalance, updatedBeneficiaryAccountBalance, moneyTransfer);
    }

    private AccountEntity creditAccountEntity(AccountEntity accountEntity, BigDecimal amountToCredit) {
        BigDecimal currentBalanceBeforeAddition = accountEntity.getAccountBalance();
        BigDecimal currentBalanceAfterAddition = currentBalanceBeforeAddition.add(amountToCredit);
        accountEntity.setAccountBalance(currentBalanceAfterAddition);
        return accountEntity;
    }

    private AccountEntity debitAccountEntity(AccountEntity accountEntity, BigDecimal amountToDebit) {
        BigDecimal currentBalanceBeforeDebit = accountEntity.getAccountBalance();
        BigDecimal currentBalanceAfterDebit = currentBalanceBeforeDebit.subtract(amountToDebit);
        accountEntity.setAccountBalance(currentBalanceAfterDebit);
        return accountEntity;
    }
}
