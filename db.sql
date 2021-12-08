CREATE TABLE messages (
    id int AUTO_INCREMENT,
    sender VARCHAR(255),
    receiver VARCHAR(255),
    message TEXT,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    read_at DATETIME
);

CREATE TABLE users (
    id int AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    avatar TEXT
);