/*
 database scheme
 Authors: Jouke Profijt, Wietse Reitsema, Kim Chau Duong
 */
drop table if exists image_annotation;
drop table if exists image_values;
drop table if exists ROI_TAGS;
drop table if exists ROI_STATE;
drop table if exists image_tags;

drop table if exists image_annotation;
drop table if exists image_values;
drop table if exists image_tags;

DROP TABLE IF EXISTS tags;
drop table if exists images_meta;


DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cache;
drop table if exists image_tags;
DROP table if exists tags;
DROP TABLE IF EXISTS file_structure;
DROP TABLE IF EXISTS directories;


-- Start actual script:
drop table if exists image_annotation;
drop table if exists image_attributes;
drop table if exists image_tags;
drop table if exists roi_points;
drop table if exists image_roi;
DROP TABLE IF EXISTS tags;
drop table if exists cache;
drop table if exists images;



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
  path        VARCHAR(700)  NOT NULL    UNIQUE,
  PRIMARY KEY (id)
);

create table image_attributes(
  id          INT                 not null    AUTO_INCREMENT,
  name        VARCHAR(600)        NOT NULL,
  path        varchar(700)        not null,
  filepath    varchar(700)        not null UNIQUE,
  date        datetime            not null,
  size        long,
  type        enum('TIFF', 'PNG', 'JPG'),
  primary key (id),
  foreign key (filepath) references images(path)
);


CREATE TABLE cache(
  image_id    INT(100)      NOT NULL,
  cache_path  VARCHAR(260)  NOT NULL    UNIQUE,
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
    roi_id      int     not null,
    image_id    int     not null,
    foreign key (image_id) references images(id),
    primary key (roi_id)
);


create table roi_points(
    id          int     not null,
    roi_id      int     not null,
    x_pos       int     not null,
    y_pos       int     not null,
    foreign key (roi_id) references image_roi(roi_id)
);


create table ROI_STATE(
 roi_id      int     not null,
 ph          double,
 T           double,
 o2          int,
 co2         int,
  FOREIGN KEY (roi_id) references image_roi(roi_id)
);

CREATE TABLE ROI_TAGS(
  roi_id     int      not null,
  tag        varchar(200),
  FOREIGN KEY (roi_id) references image_roi(roi_id)

);