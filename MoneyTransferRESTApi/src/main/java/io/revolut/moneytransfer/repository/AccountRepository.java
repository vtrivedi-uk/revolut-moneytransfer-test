package io.revolut.moneytransfer.repository;

import java.math.BigDecimal;
import java.util.List;

import io.revolut.moneytransfer.domain.AccountEntity;
import io.revolut.moneytransfer.domain.MoneyTransferEntity;

public interface AccountRepository {

    AccountEntity saveAccount(AccountEntity accountEntity);
    AccountEntity getAccountById(Long id);
    AccountEntity getAccountByEmail(String emailAddress);
    List<AccountEntity> getAllAccounts();
    void deleteAccount(Long id);
    boolean checkIfAccountExists(Long id);
    void processMoneyTransfer(AccountEntity originatorAccount, AccountEntity beneficiaryAccount, MoneyTransferEntity moneyTransfer);
    void refreshAccountBalance(AccountEntity accountEntity, BigDecimal bigDecimal) throws Exception;
}
