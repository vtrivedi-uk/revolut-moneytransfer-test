package io.revolut.moneytransfer.controllers;

import javax.inject.Inject;

import io.revolut.moneytransfer.domain.AccountEntity;
import io.revolut.moneytransfer.domain.MoneyTransferEntity;
import io.revolut.moneytransfer.service.AccountService;
import io.revolut.moneytransfer.service.MoneyTransferService;
import io.revolut.moneytransfer.util.JsonHelper;
import spark.Request;
import spark.Response;

public class MainAppControllerImpl implements MainAppController {

    private AccountService accountService;
    private MoneyTransferService moneyTransferService;

    @Inject
    MainAppControllerImpl(AccountService accountService, MoneyTransferService moneyTransferService) {
        this.accountService = accountService;
        this.moneyTransferService = moneyTransferService;
    }

    @Override
    public String createAccount(Request request, Response response) {
        AccountEntity accountEntity = JsonHelper.marshal(request.body(), AccountEntity.class);
        return JsonHelper.constructResponse(response, accountService.createAccount(accountEntity));
    }

    @Override
    public String getAllAccounts(Request request, Response response) {
        return JsonHelper.constructResponse(response, accountService.getAllAccounts());
    }

    @Override
    public String getAcountById(Request request, Response response) {
        return JsonHelper.constructResponse(response, accountService.getAccountById(request.params("id")));
    }

    @Override
    public String deleteAccountById(Request request, Response response) {
        return JsonHelper.constructResponse(response, accountService.deleteAccountById(request.params("id")));
    }

    @Override
    public String transferMoney(Request request, Response response) throws Exception {
        MoneyTransferEntity moneyTransfer = JsonHelper.marshal(request.body(), MoneyTransferEntity.class);
        String accountId = request.params("id");
        return JsonHelper.constructResponse(response, accountService.createAccountTransaction(accountId, moneyTransfer));
    }

    @Override
    public String findMoneyTransfersByAccount(Request request, Response response) {
        String accountId = request.params("id");
        return JsonHelper.constructResponse(response, moneyTransferService.getMoneyTransfersByAccountId(accountId));
    }


}
