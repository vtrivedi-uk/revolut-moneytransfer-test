package io.revolut.moneytransfer.repository;

import java.util.List;

import io.revolut.moneytransfer.domain.MoneyTransferEntity;
import io.revolut.moneytransfer.domain.MoneyTransferStatus;

public interface MoneyTransferRepository {

    /**
     * create a transaction with a completed or failed transaction status
     *
     * @param accountTransactionEntity
     * @throws Exception
     */
    void transferMoney(MoneyTransferEntity accountTransactionEntity,
                                  MoneyTransferStatus transactionStatus,
                                  String reason) throws Exception;

    /**
     * get account transactions for specified account Id
     *
     * @param accountId
     * @return
     */
    List<MoneyTransferEntity> getMoneyTransfersByAccount(Long accountId);

}
