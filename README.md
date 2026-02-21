# Console Banking System

A secure, Java-based banking application utilizing JDBC for MySQL integration. This project implements full-cycle banking operations including account management, secure authentication using SHA-256, and ACID-compliant transaction handling.



## Features

* **Robust Registration**: Comprehensive validation for Aadhaar (12 digits), PAN format (Regex), and contact details.
* **Cryptographic Security**: Sensitive data (Passwords and PINs) are never stored in plain text; they are hashed using **SHA-256**.
* **Transaction Logic**:
    * **Deposits**: Simple credit operations.
    * **Withdrawals**: Multi-factor check (Balance verify + Password/PIN authentication).
    * **Transfers**: Peer-to-peer funds transfer with verification of the receiver's identity.
* **ACID Transactions**: Uses manual `commit` and `rollback` to ensure database consistency during transfers.
* **Automated Passbook**: Generates structured reports of account info and transaction logs.

---

## Prerequisites

To run this project, you need:
* **JDK 21** or later (utilizes modern Java features like unnamed main methods).
* **MySQL Server** (8.0+ recommended).
* **MySQL Connector/J** (Add the JAR file to your project libraries).

---

## Database Schema

Run this script in your MySQL Workbench to set up the backend:

```sql
CREATE DATABASE bank;
USE bank;

-- Table for customer personal information
CREATE TABLE CUSTOMER
(
    ACCOUNT_NUMBER        BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID               VARCHAR(50) UNIQUE       NOT NULL,
    FIRST_NAME            VARCHAR(50)              NOT NULL,
    LAST_NAME             VARCHAR(50)              NOT NULL,
    FATHER_NAME           VARCHAR(50)              NOT NULL,
    FATHER_LAST_NAME      VARCHAR(50)              NOT NULL,
    AADHAAR_NUMBER        VARCHAR(12) UNIQUE       NOT NULL,
    PAN_NUMBER            VARCHAR(50) UNIQUE       NOT NULL,
    MOBILE_NUMBER         VARCHAR(10) UNIQUE       NOT NULL,
    EMAIL_ID              VARCHAR(100)   DEFAULT 'NA',
    ADDRESS               VARCHAR(100)             NOT NULL,
    CITY                  VARCHAR(50)              NOT NULL,
    STATE                 VARCHAR(80)              NOT NULL,
    PINCODE               VARCHAR(10)              NOT NULL,
    YEARLY_INCOME         DECIMAL(15, 2) DEFAULT 0,
    CURRENT_BALANCE       DECIMAL(15, 2) DEFAULT 0 NOT NULL,
    ACCOUNT_CREATION_DATE DATETIME       DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE CUSTOMER
    AUTO_INCREMENT = 100000000000;

-- Table for authentication credentials
CREATE TABLE LOGIN_INFO
(
    USER_ID        VARCHAR(50) PRIMARY KEY REFERENCES CUSTOMER (USER_ID),
    ACCOUNT_NUMBER BIGINT UNIQUE REFERENCES CUSTOMER (ACCOUNT_NUMBER),
    PASSWORD_HASH  VARCHAR(64) NOT NULL,
    PIN_HASH       VARCHAR(64) NOT NULL,
    CONSTRAINT FOREIGN KEY (ACCOUNT_NUMBER) REFERENCES CUSTOMER (ACCOUNT_NUMBER),
    CONSTRAINT FOREIGN KEY (USER_ID) REFERENCES CUSTOMER (USER_ID)
);

-- Table for tracking all credits/debits
CREATE TABLE TRANSACTION
(
    TRANSACTION_ID   BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    DATE_TIME        DATETIME DEFAULT CURRENT_TIMESTAMP,
    ACCOUNT_NUMBER   BIGINT             NOT NULL,
    TRANSACTION_TYPE VARCHAR(10)        NOT NULL CHECK ( TRANSACTION_TYPE IN ('CREDIT', 'DEBIT') ),
    DESCRIPTION      VARCHAR(200),
    AMOUNT           DECIMAL(15, 2)     NOT NULL,
    CONSTRAINT FOREIGN KEY (ACCOUNT_NUMBER) REFERENCES CUSTOMER (ACCOUNT_NUMBER)
);

-- Update trigger for automatic balance management
CREATE TRIGGER UPDATE_BALANCE
    AFTER INSERT
    ON TRANSACTION
    FOR EACH ROW
BEGIN
    IF NEW.TRANSACTION_TYPE = 'CREDIT' THEN
        UPDATE CUSTOMER
        SET CURRENT_BALANCE = CURRENT_BALANCE + NEW.AMOUNT
        WHERE ACCOUNT_NUMBER = NEW.ACCOUNT_NUMBER;
    ELSEIF NEW.TRANSACTION_TYPE = 'DEBIT' THEN
        UPDATE CUSTOMER
        SET CURRENT_BALANCE = CURRENT_BALANCE - NEW.AMOUNT
        WHERE ACCOUNT_NUMBER = NEW.ACCOUNT_NUMBER;
    END IF;
END;
```
## Setup & Execution

Follow these steps to get the Banking System running on your local machine.

### 1. Clone the Repository
```bash
git clone https://github.com/mridulsaha2008/Java-Bank-SQL.git
```

### 2. Database Configuration
Ensure your MySQL server is running. Execute the provided database script to create the `bank` database and its associated tables (`CUSTOMER`, `LOGIN_INFO`, `TRANSACTION`).


### 3. Update Connection Details
Navigate to the `Main` class in `Main.java` and replace the placeholders with your actual MySQL credentials:

```java
String url = "jdbc:mysql://localhost:3306/bank"; // Update IP and Port if necessary
String user = "YOUR_MYSQL_USERNAME";             // Your MySQL username
String pass = "YOUR_MYSQL_PASSWORD";             // Your MySQL password
```
### 4. Library Dependency
Ensure the **MySQL Connector/J** (JDBC Driver) is added to your project's build path or library folder.

### 5. Running the Application
Compile the project using the following command:
```bash
javac Main.java
```

### 6. Running the Application
Run the application:
```bash
java Main
```

## Usage Flow
The application operates through a structured console menu. Follow this flow to manage accounts:

1. **Server Connection**: On startup, the system verifies the MySQL connection.

2. **Account Creation (Option 1)**:

   * Enter personal details (Names are automatically formatted to Sentence Case).

   * Provide Aadhaar (12 digits) and PAN (Regex-validated).

   * Set a complex Password and a 6-digit PIN.

3. **Account Management (Option 2)**:

   * Enter your Account Number to view a full "Passbook" summary.

   * Includes personal details and a chronological history of all transactions.

4. **Financial Transactions (Option 3)**:

   * **Deposit**: Direct credit to the account.

   * **Withdrawal**: Requires Password or PIN authentication via SHA-256 matching.

   * **Transfer**: Move funds to another account. The system verifies the receiver's name and ensures the sender has sufficient balance.

5. **Data Safety**: The system uses manual transaction commits. If any step of a transfer fails, the entire operation is rolled back to prevent fund loss.

## Security Features
* **ACID Compliance**: Uses connection.setAutoCommit(false) to ensure transfers are atomic—if one side fails, the whole transaction rolls back.
* **Encryption**: SHA-256 Hashing ensures even database administrators cannot see plain-text passwords.

## Author
* **Mridul Saha** - Lead Developer
* [LinkedIn Profile](https://www.linkedin.com/in/mridulsaha/)
