CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE dictionary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    word VARCHAR(255) NOT NULL
);

CREATE TABLE wordLog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ans VARCHAR(255) NOT NULL,
    eatcnt INT,
    bitecnt INT
);

CREATE TABLE matchinfo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user1 INT,
    user2 INT,
    isActive BOOLEAN NOT NULL
);
