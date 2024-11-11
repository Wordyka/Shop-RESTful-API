CREATE DATABASE shop_db;

USE shop_db;

-- Create the customers table
CREATE TABLE customers (
    id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    phoneNumber VARCHAR(15) NOT NULL,
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

-- Insert data
INSERT INTO customers (phoneNumber, email, address) VALUES
('123-456-7890', 'customer1@gmail.com', '123 Main St, Sample City'),
('234-567-8901', 'customer2@gmail.com', '456 Elm St, Example Town'),
('345-678-9012', 'customer3@gmail.com', '789 Maple Ave, Demo City'),
('456-789-0123', 'customer4@gmail.com', '321 Oak St, Testville'),
('567-890-1234', 'customer5@gmail.com', '654 Pine Rd, Trial Town'),
('678-901-2345', 'customer6@gmail.com', '987 Cedar Dr, Sampleville'),
('789-012-3456', 'customer7@gmail.com', '159 Birch Blvd, Example City'),
('890-123-4567', 'customer8@gmail.com', '753 Spruce Ln, Test Town'),
('901-234-5678', 'customer9@gmail.com', '951 Willow Ct, Demo Town'),
('012-345-6789', 'customer10@gmail.com', '357 Redwood Ave, Sample City');


INSERT INTO accounts (accountNumber, accountType, accountName, accountBalance, customer_id) VALUES
('ACC1001', 'Checking', 'John Doe Checking', 1500.00, 1),
('ACC1002', 'Savings', 'John Doe Savings', 2500.50, 1),
('ACC1003', 'Checking', 'Jane Smith Checking', 1800.75, 2),
('ACC1004', 'Savings', 'Jane Smith Savings', 2200.25, 2),
('ACC1005', 'Business', 'Acme Corp Business', 5000.00, 3),
('ACC1006', 'Checking', 'Bob Johnson Checking', 1200.00, 4),
('ACC1007', 'Savings', 'Bob Johnson Savings', 3200.00, 4),
('ACC1008', 'Business', 'Smith Industries Business', 7500.00, 5),
('ACC1009', 'Checking', 'Alice Brown Checking', 1100.00, 6),
('ACC1010', 'Savings', 'Alice Brown Savings', 2100.00, 6),
('ACC1011', 'Savings', 'Charlie Green Savings', 1900.00, 7),
('ACC1012', 'Checking', 'Charlie Green Checking', 1400.00, 7),
('ACC1013', 'Business', 'Delta LLC Business', 5600.00, 8),
('ACC1014', 'Savings', 'Emily White Savings', 2700.00, 9),
('ACC1015', 'Checking', 'Emily White Checking', 1300.00, 9),
('ACC1016', 'Business', 'Omega Corp Business', 8500.00, 10),
('ACC1017', 'Checking', 'George King Checking', 1600.00, 3),
('ACC1018', 'Savings', 'George King Savings', 2400.00, 3),
('ACC1019', 'Savings', 'Henry Black Savings', 3000.00, 5),
('ACC1020', 'Checking', 'Henry Black Checking', 2000.00, 5);

-- DELETE FROM accounts;

-- DELETE FROM customers;