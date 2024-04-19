create database db;
use db;

CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Role ENUM('Admin', 'Member') NOT NULL
);

ALTER TABLE Users
MODIFY COLUMN Role ENUM('Admin', 'Member') DEFAULT 'Member' NOT NULL;


CREATE TABLE Books (
    BookID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(255),
    Author VARCHAR(255),
    Genre VARCHAR(255),
    Publisher VARCHAR(255),
    QuantityAvailable INT,
    ShelfLocation VARCHAR(255)
);

CREATE TABLE Transactions (
    TransactionID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    BookID INT,
    TransactionType ENUM('Borrow', 'Return'),
    TransactionDate DATETIME,
    DueDate DATE,
    ReturnDate DATE,
    FineAmount DECIMAL(10, 2),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);

CREATE TABLE Reservations (
    ReservationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    BookID INT,
    ReservationDate DATETIME,
    PickupDate DATETIME,
    Status ENUM('Pending', 'Confirmed', 'Cancelled'),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);
CREATE TABLE Reviews (
    ReviewID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    BookID INT,
    Rating INT,
    ReviewText TEXT,
    ReviewDate DATETIME,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);
select * from users;


