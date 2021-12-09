CREATE TABLE messages (
    id int PRIMARY KEY AUTO_INCREMENT,
    sender VARCHAR(255),
    receiver VARCHAR(255),
    message TEXT,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    read_at DATETIME
);

CREATE TABLE users (
    id int PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    avatar TEXT DEFAULT ('httpservlet.svg')
);

INSERT INTO users (username, password, email) VALUES ('ricardo', '12345', 'ricardo@email.com');
INSERT INTO users (username, password, email) VALUES ('boris', '12345', 'boris@email.com');
INSERT INTO users (username, password, email) VALUES ('rambo', '12345', 'rambo@email.com');