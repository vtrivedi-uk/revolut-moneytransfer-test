package io.revolut.moneytransfer.controllers;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.response.Response;
import io.revolut.moneytransfer.MoneyTransferApp;

public class MainAppControllerTest {

    @Before
    public void setUp() {
        MoneyTransferApp.startApp();
    }

    @After
    public void tearDown() {
        MoneyTransferApp.stopApp();
    }

    @Test
    public void addNewAccountReturn400Status() {
        given().
                when().
                body("{name:foo}").
                post("/createAccount").
                then().
                assertThat().statusCode(400);
    }

    @Test
    public void getAllAccountsReturnsBlankList() {
        Response res = get("/getAllAccounts");
        String body = res.asString();
        assertEquals(body, "[]");
    }

    @Test
    public void addNewAccountReturnsSuccessStatus201() {
        given().
                when().
                body("{name:foo,emailAddress:foo@bar.com}").
                post("/createAccount").
                then().
                assertThat().statusCode(201);
    }

    @Test
    public void getAccountByIdReturnsError404() {
        String id = "1";
        Response res = get("/getAcountById/" + id);
        assertEquals(res.statusCode(), 404);
    }

    @Test
    public void deleteAccountByIdReturnsError404() {
        String id = "1";
        Response res = delete("/deleteAccountById/" + id);
        assertEquals(res.statusCode(), 404);
    }

    @Test
    public void createNewMoneyTransferReturnsError404() {
        String accountId = "3";
        Response resp = given().
                when().
                body("{originatorAccountNumber:3,beneficiaryAccountNumber:2,transferAmount:300}").
                post("/transferMoney/" + accountId);
        assertEquals(404, resp.getStatusCode());
    }

    @Test
    public void findMoneyTransferByIdReturnsError404(){
        String accountId = "3";
        Response resp = given().
                when().
                get("/findMoneyTransfersByAccount/" + accountId);

        System.out.println("response body--"+resp.asString());
        assertEquals(404, resp.getStatusCode());
    }
}
