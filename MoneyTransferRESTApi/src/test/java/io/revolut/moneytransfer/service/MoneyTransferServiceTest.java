package io.revolut.moneytransfer.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.revolut.moneytransfer.domain.AccountEntity;
import io.revolut.moneytransfer.domain.MoneyTransferEntity;
import io.revolut.moneytransfer.domain.MoneyTransferStatus;
import io.revolut.moneytransfer.repository.AccountRepository;
import io.revolut.moneytransfer.repository.MoneyTransferRepository;

public class MoneyTransferServiceTest {

    @Mock
    private MoneyTransferRepository moneyTransferRepo;
    @Mock
    private AccountRepository accountEntityRepository;

    private MoneyTransferService moneyTransferService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        moneyTransferService = new MoneyTransferService(accountEntityRepository, moneyTransferRepo);
    }

    @Test
    public void getAllMoneyTransfersForAccountReturnsSuccess() {

        String accountId = "3";

        AccountEntity accountEntity = new AccountEntity(Long.valueOf(accountId));
        accountEntity.setEmailAddress("foo@bar.com");
        accountEntity.setName("foobar");

        Mockito.when(accountEntityRepository.checkIfAccountExists(Long.valueOf(accountId))).thenReturn(true);

        Mockito.when(moneyTransferRepo.getMoneyTransfersByAccount(Long.valueOf(accountId)))
                .thenReturn(
                        Arrays.asList(
                                new MoneyTransferEntity(Long.valueOf(accountId)
                                        , 4L
                                        , new BigDecimal("2500")
                                        , MoneyTransferStatus.SUCCESS
                                        , new Date(System.currentTimeMillis()))));

        assertEquals(200, moneyTransferService.getMoneyTransfersByAccountId(accountId).getStatusCode());
    }
}
