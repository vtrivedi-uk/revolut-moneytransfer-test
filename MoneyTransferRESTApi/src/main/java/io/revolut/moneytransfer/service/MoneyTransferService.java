package io.revolut.moneytransfer.service;

import java.util.List;

import javax.inject.Inject;

import io.revolut.moneytransfer.domain.MoneyTransferEntity;
import io.revolut.moneytransfer.domain.ApiResponseBean;
import io.revolut.moneytransfer.repository.AccountRepository;
import io.revolut.moneytransfer.repository.MoneyTransferRepository;
import io.revolut.moneytransfer.util.JsonHelper;

public class MoneyTransferService {

    private AccountRepository accountEntityRepository;
    private MoneyTransferRepository moneyTransferRepository;

    @Inject
    public MoneyTransferService(AccountRepository accountEntityRepository,
                                     MoneyTransferRepository moneyTransferRepository) {
        this.accountEntityRepository = accountEntityRepository;
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public ApiResponseBean getMoneyTransfersByAccountId(String accountId) {
        if (accountEntityRepository.checkIfAccountExists(Long.valueOf(accountId))) {
            List<MoneyTransferEntity> moneyTransfers = moneyTransferRepository.getMoneyTransfersByAccount(Long.valueOf(accountId));
            return ApiResponseBean.getSuccessBean(200, (moneyTransfers.isEmpty() ? "[]" : JsonHelper.unmarshal(moneyTransfers)));
        }
        return ApiResponseBean.getErrorBean(404, "Account with id = " + accountId + " does not exist");
    }
}
