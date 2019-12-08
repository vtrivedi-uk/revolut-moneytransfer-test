package io.revolut.moneytransfer;

import static spark.Spark.awaitInitialization;
import static spark.Spark.awaitStop;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import java.util.HashMap;

import com.google.inject.Guice;

import io.revolut.moneytransfer.controllers.MainAppController;
import spark.Spark;

public class MoneyTransferApp {

	public static final int SERVER_PORT = 8080;

	public static void main(String[] args) {
		// setup application port
		port(SERVER_PORT);

		// setup header access controls
		HashMap<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
		requestHeaders.put("Access-Control-Allow-Origin", "*");
		requestHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
		requestHeaders.put("Access-Control-Allow-Credentials", "true");
		
		// setup filters for each access control
		Spark.after((request, response) -> requestHeaders.forEach((key, value) -> response.header(key, value)));

		//instantiate the controller instance
		MainAppController controller = Guice.createInjector(new MoneyTransferAppModule()).getInstance(MainAppController.class);
		
		// setup routes
		post("/createAccount", controller::createAccount);
		get("/getAllAccounts", controller::getAllAccounts);
		get("/getAcountById/:id", controller::getAcountById);
		delete("/deleteAccountById/:id", controller::deleteAccountById);
		post("/transferMoney/:id", controller::transferMoney);
		get("/findMoneyTransfersByAccount/:id", controller::findMoneyTransfersByAccount);
	}

	/**
	 * Start the server
	 */
	public static void startApp() {
		MoneyTransferApp.main(null);
		awaitInitialization();
	}

	/**
	 * stop spark server
	 */
	public static void stopApp() {
		stop();
        awaitStop();
	}
}
