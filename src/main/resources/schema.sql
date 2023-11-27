CREATE TABLE rooms (
    id IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    user1 INT,
    user2 INT
)
CREATE TABLE users (
    id IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL
);
CREATE TABLE dictionary (
    id IDENTITY PRIMARY KEY,
    word VARCHAR NOT NULL
);
CREATE TABLE wordLog (
    id IDENTITY PRIMARY KEY,
    ans VARCHAR,
    eatcnt INT,
    bitecnt INT
);
CREATE TABLE matchinfo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user1 INT,
    user2 INT,
    isActive BOOLEAN NOT NULL
);

