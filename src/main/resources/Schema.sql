DROP TABLE IF EXISTS users;

CREATE TABLE users(
  id                 INT(100)     NOT NULL AUTO_INCREMENT,
  name               VARCHAR(40)  NOT NULL,
  email              VARCHAR(254) NOT NULL,
  password           VARCHAR(128) NOT NULL,
  role               VARCHAR(6)   NOT NULL,
  enabled            TINYINT,
  created_date       DATETIME,
  password_token     VARCHAR(200),
  password_timestamp DATETIME,
  PRIMARY KEY (id)

);