package io.revolut.moneytransfer.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import io.revolut.moneytransfer.domain.AccountEntity;
import io.revolut.moneytransfer.domain.MoneyTransferEntity;
import io.revolut.moneytransfer.domain.MoneyTransferStatus;

public class AccountRepositoryImpl implements AccountRepository {


    private EntityManager entityManager;

    @Inject
    public AccountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AccountEntity saveAccount(AccountEntity accountEntity) {
        entityManager.getTransaction().begin();
        entityManager.persist(accountEntity);
        entityManager.getTransaction().commit();
        return getAccountByEmail(accountEntity.getEmailAddress());
    }

    @Override
    public boolean checkIfAccountExists(Long id) {
        return Objects.nonNull(getAccountById(id));
    }

    @Override
    public void refreshAccountBalance(AccountEntity accountEntity, BigDecimal bigDecimal) throws Exception {
        try {
            Long accountId = accountEntity.getId();
            Query query = entityManager.createQuery("update " + AccountEntity.class.getName() + " acc set acc.accountBalance=?1 where acc.id=?2");
            query.setParameter(1, bigDecimal);
            query.setParameter(2, accountId);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
    @Override
    public void processMoneyTransfer(AccountEntity originatorAccount, AccountEntity beneficiaryAccount, MoneyTransferEntity moneyTransfer) {
        entityManager.getTransaction().begin();
        entityManager.persist(originatorAccount);
        entityManager.persist(beneficiaryAccount);
        entityManager.persist(updateTransactionEntity(moneyTransfer, MoneyTransferStatus.SUCCESS, ""));
        entityManager.getTransaction().commit();
    }

    private MoneyTransferEntity updateTransactionEntity(MoneyTransferEntity accountTransactionEntity, MoneyTransferStatus transactionStatus, String reason) {
        accountTransactionEntity.setTransferStatus(transactionStatus);
        accountTransactionEntity.setReason(reason);
        accountTransactionEntity.setTransferDate(new Date(System.currentTimeMillis()));
        return accountTransactionEntity;
    }

    @Override
    public AccountEntity getAccountById(Long id) {
        AccountEntity accountEntity = null;
        try {
            Query query = entityManager.createQuery("from " + AccountEntity.class.getName() + " acc where acc.id = ?1");
            query.setParameter(1, id);
            accountEntity = (AccountEntity) query.getSingleResult();
        } catch (Exception ex) {
            return accountEntity;
        }
        return accountEntity;
    }

    @Override
    public AccountEntity getAccountByEmail(String emailAddress) {
        AccountEntity accountEntity = null;
        try {
            Query query = entityManager.createQuery("from " + AccountEntity.class.getName() + " acc where acc.emailAddress = ?1");
            query.setParameter(1, emailAddress);
            accountEntity = (AccountEntity) query.getSingleResult();
        } catch (Exception ex) {
            return accountEntity;
        }
        return accountEntity;
    }

    @Override
    public List<AccountEntity> getAllAccounts() {
        Query query = entityManager.createQuery("from " + AccountEntity.class.getName() + " a");
        List<AccountEntity> accountEntities = query.getResultList();
        return accountEntities;
    }

    @Transactional
    @Override
    public void deleteAccount(Long id) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("delete from " + AccountEntity.class.getName() + " acc where acc.id = ?1");
        query.setParameter(1, id);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }
}
