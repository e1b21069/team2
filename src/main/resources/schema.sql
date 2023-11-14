CREATE TABLE users (
    id IDENTITY PRIMARY KEY,
    userName VARCHAR NOT NULL
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
