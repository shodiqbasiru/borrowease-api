# BorrowEase - Online Loan Application

BorrowEase is an online loan application that allows users to apply for loans, make installment payments,
and integrate with Midtrans for payment processing. The application features loan transaction management,
including payment terms and installment status.

## Features

- **User Authentication**: Users can register and log in to the application with a user account.
- **Loan Application**: Users can apply for loans by selecting the term and loan amount.
- **Installment Payments**: Users can make loan installment payments using various payment methods through Midtrans.
- **Loan Management**: The application supports managing loan statuses such as `ON_PROGRESS` and `COMPLETED`.
- **Midtrans Integration**: The payment system is integrated with Midtrans to provide various payment methods (Virtual
  Account, e-wallet, etc.).
- **Loan Limit Increase**: When a loan is paid off, the user's loan limit will automatically increase.

## Technologies Used

- **Java**: Main programming language.
- **Spring Boot**: Backend framework for building the application.
- **Hibernate/JPA**: Used for ORM and database interaction.
- **Midtrans API**: Used for handling online payments.
- **PostgreSQL**: Database for storing application data.
- **Scheduler**: For scheduling certain tasks such as transaction status checks.
- **JWT (JSON Web Token)**: For user authentication.
- **Swagger**: For API documentation.

## Development Status

This API application is still under development and will continue to be enhanced with new features and improvements.

## Arsitektur Entitas

### User

This entity manages application user data, including information about the user's name, email, password, and role.

- **Main Fields**:
    - `id`: Unique user ID.
    - `username`: User's username.
    - `password`: User's password.
    - `role`: User's role (`CUSTOMER`, `STAFF`, `ADMIN`).
    - `deletedAt`: Date the user was removed from the application.
    - `customer`: Information related to the customer associated with the user.
    - `staff`: Information related to the staff associated with the user.

### Customer

This entity manages customer data, including information about the customer's name, address, phone number, and loan limit.

- **Main Fields**:
  - `id`: Unique customer ID.
  - `firstName`: Customer's first name.
  - `lastName`: Customer's last name.
  - `motherName`: Customer's mother's name.
  - `status`: Customer's status (`MARRIED`, `SINGLE`, `DIVORCED`, `WIDOWED`).
  - `phoneNumber`: Customer's phone number.
  - `email`: Customer's email address.
  - `dateOfBirth`: Customer's date of birth.
  - `address`: Customer's address.
  - `lastEducation`: Customer's last education.
  - `npwp`: Customer's NPWP number.
  - `Job`: Information related to the customer's job.
  - `user`: User associated with the customer.
  - `loanLimit`: Customer's loan limit.

### Staff

This entity manages staff data, including information about the staff's name, address, phone number, and position.

- **Main Fields**:
  - `id`: Unique staff ID.
  - `firstName`: Staff's first name.
  - `lastName`: Staff's last name.
  - `phoneNumber`: Staff's phone number.
  - `email`: Staff's email address.
  - `address`: Staff's address.
  - `user`: User associated with the staff.

### LoanLimit

This entity manages customer loan limit data, including information about the loan limit and remaining loan limit.

- **Main Fields**:
  - `id`: Unique loan limit ID.
  - `initialLimit`: Initial loan limit.
  - `currentLimit`: Remaining loan limit.
  - `updatedDate`: Date the loan limit was last updated.

### Job

This entity manages customer job data, including information about the company name, position, and income.

- **Main Fields**:
  - `id`: Unique job ID.
  - `jobName`: Job name.
  - `companyName`: Company name.
  - `salary`: Customer's income.

### Identity Card

This entity manages customer identity card data, including information about the type of identity card, card number, and validity date.

- **Main Fields**:
  - `id`: Unique identity card ID.
  - `identityCard`: Type of identity card.
  - `identityNumber`: Identity card number.
  - `customer`: Information related to the customer who owns the identity card.

### LoanTrx

This entity manages main loan transaction data, including information about the loan type, loan amount, installments, interest, and loan process status.

- **Main Fields**:
  - `id`: Unique loan transaction ID.
  - `loanTrxDate`: Loan transaction date.
  - `loanType`: Loan type.
  - `amount`: Loan amount.
  - `termMonth`: Loan term in months.
  - `installment`: Installment amount.
  - `interestRate`: Loan interest rate.
  - `installmentAmount`: Monthly installment amount.
  - `loanProcess`: Loan process status (`ON_PROGRESS`, `COMPLETED`, `REJECTED`, `APPROVED`, `PENDING`).
  - `customer`: Information related to the customer who took the loan.

### LoanTrxDetail

This entity records each loan installment detail, including the installment amount and due date.

- **Main Fields**:
  - `id`: Unique loan detail ID.
  - `dueDate`: Installment due date.
  - `paymentAmount`: Amount to be paid in the installment.
  - `status`: Installment status (`UNPAID`, `PENDING`, `PAID`).
  - `loanTrx`: Related loan transaction.
  - `payment`: Related payment.

### Payment

This entity manages payment data sent to Midtrans for installment payment processing.

- **Main Fields**:
  - `id`: Unique payment ID.
  - `token`: Payment token from Midtrans.
  - `redirectUrl`: URL to redirect the user to the Midtrans payment page.
  - `transactionStatus`: Payment transaction status.

## API Documentation

### User Authentication

#### Register Customer

Request:

- Endpoint: `/api/auth/register`
- Method: `POST`
- Header:
  - `Content-Type: application/json`
  - `Accept: application/json`
- Body:

```json
{
  "firstName": "string",
  "lastName": "string",
  "motherName": "string",
  "status": "string",
  "username": "string",
  "password": "string",
  "email": "string",
  "phoneNumber": "string",
  "birthDate": "string",
  "address": "string",
  "lastEducation": "string",
  "identityCard": "string",
  "identityNumber": "string",
  "jobName": "string",
  "companyName": "string",
  "salary": 0,
  "npwp": "string"
}
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string",
    "status": "string",
    "username": "string",
    "role": "string",
    "phoneNumber": "string",
    "email": "string",
    "identityCard": "string",
    "jobName": "string",
    "npwp": "string",
    "birthDate": "string",
    "createdAt": "string",
    "updatedAt": "string"
  }
}
```

#### Register Staff

Request:

- Endpoint: `/api/auth/register-staff`
- Method: `POST`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
- Body:

```json
{
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "password": "string",
  "email": "string",
  "phoneNumber": "string",
  "address": "string"
}
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "email": "string",
    "role": "string",
    "phoneNumber": "string",
    "address": "string",
    "createdAt": "string",
    "updatedAt": "string"
  }
}
```

#### Login

Request:

- Endpoint: `/api/auth/login`
- Method: `POST`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
- Body:

```json
{
  "username": "string",
  "password": "string"
}
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "userId": "string",
    "customerId": "string",
    "username": "string",
    "token": "string",
    "role": "string"
  }
}
```

### Customer

#### Get Customer Profile

Request:

- Endpoint: `/api/customers/{customerId}`
- Method: `GET`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Path Variable: `{customerId}`

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string",
    "createdAt": "string",
    "updatedAt": "string",
    "id": "string"
  }
}
```

#### Get All Customers

Request:

- Endpoint: `/api/customers`
- Method: `GET`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": [
    {
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "phoneNumber": "string",
      "address": "string",
      "createdAt": "string",
      "updatedAt": "string",
      "id": "string"
    }
  ]
}
```

#### Update Customer Profile

Request:

- Endpoint: `/api/customers`
- Method: `PUT`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Body:

```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phoneNumber": "string",
  "address": "string"
}
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string",
    "createdAt": "string",
    "updatedAt": "string",
    "id": "string"
  }
}
```

### Staff

#### Get Staff Profile

Request:

- Endpoint: `/api/staffs/{staffId}`
- Method: `GET`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Path Variable: `{staffId}`

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string"
  }
}
```

#### Get All Staffs

Request:

- Endpoint: `/api/staffs`
- Method: `GET`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": [
    {
      "id": "string",
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "phoneNumber": "string",
      "address": "string"
    }
  ]
}
```

#### Update Staff Profile

Request:

- Endpoint: `/api/staffs`
- Method: `PUT`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Body:

```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phoneNumber": "string",
  "address": "string"
}
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string"
  }
}
```

### Loan Management

#### Apply Loan

Request:

- Endpoint: `/api/loans/create-application-loan`
- Method: `POST`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Body:

```json
{
  "customerId": "string",
  "loanType": "string",
  "amount": 0,
  "termMonth": 0
}
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "customerId": "string",
    "loanType": "string",
    "amount": 0,
    "loanProcess": "string"
  }
}
``` 

#### Approve Loan

Request:

- Endpoint: `/api/loans/approval-loan`
- Method: `PUT`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Body:

```json
{
  "loanId": "string",
  "loanProcess": "string"
}
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "customerId": "string",
    "loanType": "string",
    "amount": 0,
    "termMonth": 0,
    "installment": "string",
    "interestRate": "string",
    "installmentAmount": 0,
    "loanTrxDetails": [
      {
        "id": "string",
        "loanId": "string",
        "dueDate": "string",
        "paymentAmount": 0,
        "status": "string"
      }
    ]
  }
}
```

#### Get Loan Transactions

Request:

- Endpoint: `/api/loans/{loanId}`
- Method: `GET`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Path Variable: `{loanId}`

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "customerId": "string",
    "loanType": "string",
    "amount": 0,
    "termMonth": 0,
    "installment": "string",
    "interestRate": "string",
    "installmentAmount": 0,
    "loanTrxDetails": [
      {
        "id": "string",
        "loanId": "string",
        "dueDate": "string",
        "paymentAmount": 0,
        "status": "string"
      }
    ]
  }
}
```

#### Get All Loan Transactions

Request:

- Endpoint: `/api/loans`
- Method: `GET`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": [
    {
      "id": "string",
      "customerId": "string",
      "loanType": "string",
      "amount": 0,
      "termMonth": 0,
      "installment": "string",
      "interestRate": "string",
      "installmentAmount": 0,
      "loanTrxDetails": [
        {
          "id": "string",
          "loanId": "string",
          "dueDate": "string",
          "paymentAmount": 0,
          "status": "string"
        }
      ]
    }
  ]
}
```

#### Payment Loan

Request:

- Endpoint: `/api/loans/payment-loan`
- Method: `POST`
- Header:
    - `Content-Type: application/json`
    - `Accept: application/json`
    - `Authorization: Bearer {token}`
- Body:

```json
[
  {
    "loanTrxDetailId": "string",
    "amount": 0
  }
]
```

Response:

```json
{
  "message": "string",
  "statusCode": 0,
  "data": {
    "id": "string",
    "token": "string",
    "redirectUrl": "string",
    "loanTrxStatus": "string",
    "detailPayments": [
      {
        "id": "string",
        "amount": 0
      }
    ]
  }
}
```

