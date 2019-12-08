# revolut-moneytransfer-test
Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

# Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like ( except Spring ), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

# Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

# Tech Stack

## Development:
  - Java-8
  - Gradle
  - SparkJava
  - Guice
  - Hibernate JPA
  - H2 DB

## Testing:
   - Junit
   - Mockito

## Design approach:
  - REST API to invoke service methods 
  - customized MVC style design
  - stateless
  - Concurrent request handling support for shared-state. (Thread Safe)
  - Atomic Transactions with 3-phase commit for fund transfers
 ### Dependency Injection Design:

- MoneyTransferApp business logic holder needs AccountService, MoneyTransferService for it’s business.
- It does not create them but asks for them (the dependencies).
- The factory (using Guice) creates those dependencies and wire them to MoneyTransferApp. 
- The main method in the application launcher first instantiates the factory, the factory in turn instantiates the Controller and injects the dependencies. 
 Summary: Create the factory, create the application using the factory
 and then start the application!

  
# Public Interfaces of the MoneyTransfer API
- Create Accounts
- Get All Accounts
- Get Account By ID
- Delete account by ID
- Transfer Money
- Find all money transfers for a user

## Create an account
The following request creates an account and returns empty body with 201 status code
```
    POST localhost:8080/createAccount
    {
        "name": "alpha",
        "emailAddress": "alpha@gamma.com",
        "accountBalance": 5000
    }
```
Response:
```
    HTTP 201 Created
    <Response body is empty>
```
## Get All Accounts 
The following request gets all accounts :
```
    GET http://localhost:8080/getAllAccounts
    
```
Response:
```
    HTTP 200 OK
    [
		{
			"id": 1,
			"name": "alpha",
			"emailAddress": "alpha@gamma.com",
			"accountBalance": 5000
		},
		{
			"id": 2,
			"name": "beta",
			"emailAddress": "beta@gamma.com",
			"accountBalance": 6000
		}
	]
```
## Get Account by ID
The following request gets all accounts :
```
    GET http://localhost:8080/getAcountById/1
    
```
Response:
```
    HTTP 200 OK
    {
		"id": 1,
		"name": "alpha",
		"emailAddress": "alpha@gamma.com",
		"accountBalance": 5000
	}
```

## Delete Account by ID
The following request deletes an account:
```
    DELETE localhost:8080/deleteAccountById/0
```
Response:
```
    HTTP 204 No Content
```

## Money Transfer Transaction
Transfer money from one account to another [Account id #1 will be debited and account id #2 will be credited with 3000 ]:
```
    POST http://localhost:8080/transferMoney/1 
    {
		originatorAccountNumber:1,
		beneficiaryAccountNumber:2,
		transferAmount:3000
	}
```
Response:
```
    HTTP 201 No Content
```
## Retrieve all money transfer transactions on specific account id
The folowing request retrieves all money transfer transactions on specific account id
```
    GET http://localhost:8080/findMoneyTransfersByAccount/1
```
Response:
```
    HTTP 200 OK
    [
		{
			"id": 3,
			"originatorAccountNumber": 1,
			"beneficiaryAccountNumber": 2,
			"transferAmount": 3000,
			"transferStatus": "SUCCESS",
			"transferDate": "Dec 8, 2019",
			"reason": ""
		}
	]

```
## Usage
### Quick Start - Run
1. In Project Root Directory, type <br/>
```$xslt
 java -jar money-transfer-api-1.0.jar
```
This will start the application on http://localhost:8080 ,if the port is not used by another process.
or
```$xslt
1) ./gradlew build
2) ./gradlew start
```
This will start the application on http://localhost:8080 ,if the port is not used by another process.
### Run All Tests[Make sure nothing is running on port 8080]
```$xslt
./gradlew test
```