DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS images;

CREATE TABLE users(
  id        INT(100)     NOT NULL AUTO_INCREMENT,
  name      VARCHAR(40)  NOT NULL,
  email     VARCHAR(254) NOT NULL,
  password  VARCHAR(128) NOT NULL,
  role      VARCHAR(6)   NOT NULL,
  PRIMARY KEY (id)

);

CREATE TABLE images(
  id        INT(100)    NOT NULL AUTO_INCREMENT,
  orig_name VARCHAR(50)  NOT NULL,
  hash_name VARCHAR(50)  NOT NULL,
  path      VARCHAR(50)  NOT NULL,

  PRIMARY KEY (id)
);