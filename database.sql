CREATE DATABASE shop_cart;

USE shop_cart;

-- Create the customers table
CREATE TABLE customers (
    id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    phone_number VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Create the accounts table
CREATE TABLE accounts (
    accountNumber VARCHAR(20) NOT NULL,
    accountType VARCHAR(50) NOT NULL,
    accountName VARCHAR(100) NOT NULL,
    accountBalance DECIMAL(15, 2) NOT NULL CHECK (accountBalance >= 0),
    customer_id BIGINT(20) UNSIGNED NOT NULL,
    PRIMARY KEY (accountNumber),
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Verify the table structure
DESC customers;
DESC accounts;

DELETE FROM accounts;

DELETE FROM customers;