package io.revolut.moneytransfer.repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import io.revolut.moneytransfer.domain.MoneyTransferEntity;
import io.revolut.moneytransfer.domain.MoneyTransferStatus;

import java.sql.Date;
import java.util.List;

public class MoneyTransferRepositoryImpl implements MoneyTransferRepository {

    private EntityManager entityManager;

    @Inject
    public MoneyTransferRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void transferMoney(MoneyTransferEntity accountTransactionEntity, MoneyTransferStatus transactionStatus, String reason) throws Exception {
        try {
            entityManager.getTransaction().begin();
            accountTransactionEntity.setTransferDate(new Date(System.currentTimeMillis()));
            accountTransactionEntity.setTransferStatus(transactionStatus);
            accountTransactionEntity.setReason(reason);
            entityManager.persist(accountTransactionEntity);
            entityManager.getTransaction().commit();
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public List<MoneyTransferEntity> getMoneyTransfersByAccount(Long accountId) {
        Query query = entityManager.createQuery("from " + MoneyTransferEntity.class.getName() + " a");
        List<MoneyTransferEntity> accountTransactionEntities = query.getResultList();
        accountTransactionEntities.forEach(accountTransactionEntity -> System.out.println("\ntransaction entity = " + accountTransactionEntity.toString()));
        return accountTransactionEntities;
    }
}
