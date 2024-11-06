# Account API Spec

This document specifies the endpoints and expected behavior for managing account data.

## Get All Accounts

Endpoint: GET /api/accounts


Response Body (Success) :

```json
[
	{
   	   "accountNumber": "123456789",
   	   "accountType": "Savings",
   	   "accountName": "Savings Account",
   	   "accountBalance": 5000.00
   	   "customerID" : 1
   	 },
	{
   	   "accountNumber": "12314234",
   	   "accountType": "Checking",
   	   "accountName": "Primary Account",
   	   "accountBalance": 4500.75
   	   "customerID" : 2
   	 }
]

```

## Get Account By Number

Endpoint: GET /api/accounts/{id}

Path Parameter : 

```json
{
	"id" : "1"
}
```


Response Body (Success) :

```json
{
   "accountNumber": "123456789",
   "accountType": "Savings",
   "accountName": "Savings Account",
   "accountBalance": 5000.00
   "customerID" : 1
 }

```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "Account not found"
}
```


## Create Account

Endpoint : POST /api/customers/{customerId}/accounts

Request Body : 

```json
	"accountType": "Savings",
	"accountName": "Savings Account",
	"accountBalance": 5000.00,
	"customerID": 1
```


Response Body (Success) :

```json
{
	"data" : {
      "accountType": "Savings",
      "accountName": "Savings Account",
      "accountBalance": 5000.00,
      "customerID": 1
    }
}
```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "accountType must not be blank, accountName must be valid, accountBalance must be a positive number"
}
```


## Update Account

Endpoint : PUT /api/accounts/{id}

Path Parameter : 

```json
{
	"id" : 1
}
```

Request Body : 

```json
{
	"accountType": "Checking",
	"accountName": "Updated Account Name",
	"accountBalance": 6000.00,
	"customerID": 1
}

```


Response Body (Success) :

```json
{
	"data": "Account updated successfully"
}
```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "accountType must not be blank, accountName must be valid, accountBalance must be a positive number, account not found"
}
```

## Delete Account


Endpoint: DELETE /api/accounts/{id}

Path Parameter : 

```json
{
	"id" : 1
}
```


Response Body (Success) :

```json
{
	"data": "Account deleted successfully"
}
```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "customer not found"
}
```