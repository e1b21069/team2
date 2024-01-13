CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    roomId INT
);

CREATE TABLE dictionary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    word VARCHAR(255) NOT NULL
);

CREATE TABLE wordLog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roomId INT,
    userId INT,
    ans VARCHAR(255) NOT NULL,
    eatcnt INT,
    bitecnt INT
);
CREATE TABLE matchinfo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roomName VARCHAR(255) NOT NULL,
    pplNum INT NOT NULL,
    isActive BOOLEAN NOT NULL
);
CREATE TABLE match (
    id IDENTITY PRIMARY KEY,
    roomId INT,
    word VARCHAR,
    playerNum INT DEFAULT 0
);

CREATE TABLE wordleLog(
    id INT AUTO_INCREMENT PRIMARY KEY,
    roomId INT,
    userId INT,
    ans VARCHAR(255) NOT NULL,
    result INT
);
