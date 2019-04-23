DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cache;
drop table if exists image_tags;
drop table if exists roi;
drop table if exists meta_data;
DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS file_structure;
DROP TABLE IF EXISTS directories;


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

CREATE TABLE images(
  id          INT(100)      NOT NULL    AUTO_INCREMENT,
  orig_name   VARCHAR(500)  NOT NULL,
  new_name    VARCHAR(500)  NOT NULL,
  path        VARCHAR(260)  NOT NULL    UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE cache(
  image_id    INT(100)      NOT NULL,
  cache_path  VARCHAR(260)  NOT NULL    UNIQUE,
  PRIMARY KEY (image_id),
  FOREIGN KEY (image_id) REFERENCES images(id)
);

CREATE TABLE directories(
  id          INT           NOT NULL    auto_increment,
  creator     varchar(100),
  primary key (id)
);

create table file_structure(
  directory_id      int,
  subdirectory_id   int,
  FOREIGN KEY (directory_id) REFERENCES directories(id),
  FOREIGN KEY (subdirectory_id) REFERENCES directories(id)
);