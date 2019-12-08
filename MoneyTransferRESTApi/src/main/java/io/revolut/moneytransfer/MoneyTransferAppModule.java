package io.revolut.moneytransfer;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.revolut.moneytransfer.controllers.MainAppController;
import io.revolut.moneytransfer.controllers.MainAppControllerImpl;
import io.revolut.moneytransfer.repository.AccountRepository;
import io.revolut.moneytransfer.repository.AccountRepositoryImpl;
import io.revolut.moneytransfer.repository.MoneyTransferRepository;
import io.revolut.moneytransfer.repository.MoneyTransferRepositoryImpl;

public class MoneyTransferAppModule extends AbstractModule {

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();

    @Override
    protected void configure() {
        bind(MainAppController.class).to(MainAppControllerImpl.class);
        bind(AccountRepository.class).to(AccountRepositoryImpl.class);
        bind(MoneyTransferRepository.class).to(MoneyTransferRepositoryImpl.class);
    }

    @Provides
    @Singleton
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("db-manager");
    }

    @Provides
    public EntityManager createEntityManager(
            EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
        if (entityManager == null) {
            ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory.createEntityManager());
        }
        return entityManager;
    }
}
