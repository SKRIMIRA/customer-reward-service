# Customer Rewards Service

This project is a Spring Boot application that calculates rewards for customers based on their transaction history. The application provides RESTful endpoints to calculate and retrieve rewards.

## Overview

The Customer Rewards Service calculates rewards points for customers based on their transaction amounts. The rewards points are calculated as follows:
- For transaction amounts greater than $100, 2 points are awarded for every dollar spent over $100.
- For transaction amounts between $50 and $100, 1 point is awarded for every dollar spent over $50.

The application includes the following main components:
- REST Controller to handle HTTP requests.
- Service to perform the rewards calculation.
- Repository to manage transaction data.

## Technologies Used

- Java 8
- Spring Boot 3.x
- Spring Data JPA
- H2 Database (In memory DB)
- JUnit 5 (for testing)
- Mockito (for mocking dependencies)
- Jackson (for JSON processing)
- Lombok

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/SKRIMIRA/customer-reward-service.git
   cd customer-reward-service
   
2. Application start on port 8080.

3. This application run data.sql file on start to insert the dummy transactions data.

## API endpoint

1. This endpoint calculates the monthly and total rewards for each customer based on the transaction date. Users are required to pass the transactionDate in the provided format. If the transactionDate is not provided, the report will be generated based on the current date.


- `GET /rewards/calculate?transactionDate=dd/MM/yyyy`

```
   curl --location 'http://localhost:8080/rewards/calculate?transactionDate=19%2F02%2F2025'
```


2. This endpoint insert the transaction records.


- `POST /rewards/insertTransactions`

```
curl --location 'http://localhost:8080/rewards/insertTransactions' \
--header 'Content-Type: application/json' \
--data '[{
    "customerId": 1,
    "transactionDate": "2024-01-15T00:00:00",
    "amount": 120
},
{
    "customerId": 2,
    "transactionDate": "2024-02-17T00:00:00",
    "amount": 110
}]'
```


