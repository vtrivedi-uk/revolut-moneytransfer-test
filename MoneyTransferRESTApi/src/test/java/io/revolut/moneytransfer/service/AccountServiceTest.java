package io.revolut.moneytransfer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.revolut.moneytransfer.domain.AccountEntity;
import io.revolut.moneytransfer.domain.ApiResponseBean;
import io.revolut.moneytransfer.repository.AccountRepository;
import io.revolut.moneytransfer.repository.MoneyTransferRepository;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountEntityRepository;
    @Mock
    private MoneyTransferRepository moneyTransferRepo;
    private AccountService accountService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountService(accountEntityRepository, moneyTransferRepo);
    }

    @Test
    public void addNewAccountReturnsSuccess201() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmailAddress("foo@bar.com");
        accountEntity.setName("foobar");
        Mockito.when(accountEntityRepository.saveAccount(accountEntity)).thenReturn(accountEntity);
        ApiResponseBean endpointOperationResponsePayload = accountService.createAccount(accountEntity);
        assertEquals(endpointOperationResponsePayload.getStatusCode(), 201);
    }

    @Test
    public void addNewAccountWithNoEmailReturnsError400() {
        AccountEntity accountEntity = new AccountEntity();
        Mockito.when(accountEntityRepository.saveAccount(accountEntity)).thenReturn(accountEntity);
        ApiResponseBean endpointOperationResponsePayload = accountService.createAccount(accountEntity);
        assertTrue(endpointOperationResponsePayload.getStatusCode() == 400);
    }

    @Test
    public void addNewAccountWithNoStatusReturnsError400() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmailAddress("");
        accountEntity.setName("foobar");
        Mockito.when(accountEntityRepository.saveAccount(accountEntity)).thenReturn(accountEntity);
        ApiResponseBean endpointOperationResponsePayload = accountService.createAccount(accountEntity);
        assertEquals(endpointOperationResponsePayload.getStatusCode(), 400);
    }

    @Test
    public void addNewAccountWithInvalidEmailReturnsError400() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmailAddress("invalid email");
        accountEntity.setName("foobar");
        Mockito.when(accountEntityRepository.saveAccount(accountEntity)).thenReturn(accountEntity);
        ApiResponseBean endpointOperationResponsePayload = accountService.createAccount(accountEntity);
        assertEquals(endpointOperationResponsePayload.getStatusCode(), 400);
    }

    @Test
    public void deleteAccountReturnsSuccess() {
        long id = 3;
        AccountEntity accountEntity = new AccountEntity(id);
        accountEntity.setEmailAddress("foo@bar.com");
        accountEntity.setName("foobar");
        Mockito.when(accountEntityRepository.getAccountById(id)).thenReturn(accountEntity);
        ApiResponseBean response = accountService.deleteAccountById(String.valueOf(id));
        assertEquals(response.getStatusCode(), 204);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNewAccountWithNulldataReturnsException() {
        ApiResponseBean response = accountService.createAccount(null);
        assertEquals(response.getStatusCode(), 400);
    }
}
