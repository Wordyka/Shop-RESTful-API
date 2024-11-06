# Customer API Spec

This document specifies the endpoints and expected behavior for managing customer data.

## Get All Customers (Paginated)

Endpoint: GET /api/customers

Query Parameters:
- page (integer, optional): The page number to retrieve, starting from 0.
- size (integer, optional): The number of records per page.

Response Body (Success) :

```json
{
	"data": [
		{
			"id": 1,
			"phoneNumber": "123-456-7890",
			"email": "customer@gmail.com",
			"address": "123 Main St, Sample City",
			"accounts": [
				{
					"accountNumber": "987654321",
					"accountType": "Checking",
					"accountName": "Primary Account",
					"accountBalance": 2500.75
				},
				{
					"accountNumber": "123456789",
					"accountType": "Savings",
					"accountName": "Savings Account",
					"accountBalance": 5000.00
				}
			]
		}
	],
	"page": {
		"size": 1,
		"totalPages": 1,
		"currentPage": 0
	}
}

```

## Get Customer By Id

Endpoint: GET /api/customers/{id}

Path Parameter : 

```json
{
	"id" : 1
}
```


Response Body (Success) :

```json
{
	"id": 1,
	"phoneNumber": "123-456-7890",
	"email": "customer@gmail.com",
	"address": "123 Main St, Sample City",
	"accounts": [
		{
			"accountNumber": "987654321",
			"accountType": "Checking",
			"accountName": "Primary Account",
			"accountBalance": 2500.75
		},
		{
			"accountNumber": "123456789",
			"accountType": "Savings",
			"accountName": "Savings Account",
			"accountBalance": 5000.00
		}
	]
}

```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "Customer not found"
}
```


## Create Customer

Endpoint : POST /api/customers

Request Body : 

```json
{
	"phoneNumber": "66131737123",
	"email": "user@gmail.com",
	"address": "Jakarta",
	"accounts": [
		{
			"accountNumber": "123456789",
			"accountType": "Savings",
			"accountName": "Savings Account",
			"accountBalance": 5000.00
		}
	]
}
```


Response Body (Success) :

```json
{
	"data" : "Customer created successfully"
}
```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "Invalid input: phoneNumber must not be blank, email must be valid, address must not exceed 255 characters"
}
```


## Update Customer

Endpoint : PUT /api/customers/{id}

Path Parameter : 

```json
{
	"id" : 1
}
```

Request Body : 

```json
{
	"phoneNumber": "66131737123",
	"email": "updated_user@gmail.com",
	"address": "New Jakarta"
}

```


Response Body (Success) :

```json
{
	"data": "Customer updated successfully"
}
```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "Customer not found, phoneNumber must not be blank, email must be valid"
}
```

## Delete Customer


Endpoint: DELETE /api/customers/{id}

Path Parameter : 

```json
{
	"id" : 1
}
```


Response Body (Success) :

```json
{
	"data": "Customer deleted successfully"
}
```

Response Body (Failed) :

```json
{
	"data": "Error",
	"errors": "customer not found"
}
```