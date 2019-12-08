package io.revolut.moneytransfer.controllers;

import spark.Request;
import spark.Response;
public interface MainAppController {
    String createAccount(Request request, Response response);
    String getAllAccounts(Request request, Response response);
    String getAcountById(Request request, Response response);
    String deleteAccountById(Request request, Response response);
    String transferMoney(Request request, Response response) throws Exception;
    String findMoneyTransfersByAccount(Request request,Response response);
}
