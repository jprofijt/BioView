/*
 database scheme
 Authors: Jouke Profijt, Wietse Reitsema, Kim Chau Duong
 */
drop table if exists image_annotation;
drop table if exists image_values;
drop table if exists ROI_TAGS;
drop table if exists ROI_STATE;
drop table if exists image_roi;
drop table if exists image_tags;

drop table if exists image_annotation;
drop table if exists image_values;
drop table if exists image_tags;
drop table if exists image_roi;
DROP TABLE IF EXISTS tags;
drop table if exists images_meta;


DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cache;
drop table if exists image_tags;
DROP table if exists tags;
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
  role               enum('ADMIN', 'USER')   NOT NULL,
  enabled            TINYINT,
  created_date       DATETIME,
  password_token     VARCHAR(200),
  password_timestamp DATETIME,
  PRIMARY KEY (id)

);

CREATE TABLE images(
  id          INT           NOT NULL    AUTO_INCREMENT,
  orig_name   VARCHAR(500)  NOT NULL,
  new_name    VARCHAR(500)  NOT NULL,
  path        VARCHAR(260)  NOT NULL    UNIQUE,
  PRIMARY KEY (id)
);

create table images_meta(
    id          INT                 not null    unique,
    path        varchar(200)        not null    unique,
    date        datetime            not null,
    size        long,
    type    enum('TIFF', 'PNG', 'JPEG'),
    primary key (id)
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

create table tags(
    tag         varchar(100)        not null    unique,
    primary key (tag)
);

create table image_tags(
   image_id    int             not null,
   image_tag   varchar(100)    not null,
   foreign key (image_id)  references images(id),
   foreign key (image_tag) references tags(tag)
);

create table image_annotation(
    image_id    int             not null,
    annotation        varchar(500)    not null,
    foreign key (image_id)  references images(id)
);

create table image_roi(
  roi_id      int     not null    AUTO_INCREMENT,
  image_id    int     not null,
  x1          int     not null,
  y1          int     not null,
  x2          int     not null,
  y2          int     not null,
  foreign key (image_id) references images(id),
  primary key (roi_id)
);

create table ROI_STATE(
 roi_id      int     not null,
 ph          double,
 T           int,
 o2          int,
 co2         int,
  FOREIGN KEY (roi_id) references image_roi(roi_id)
);

CREATE TABLE ROI_TAGS(
  roi_id     int      not null,
  tag        varchar(200),
  FOREIGN KEY (roi_id) references image_roi(roi_id)

);