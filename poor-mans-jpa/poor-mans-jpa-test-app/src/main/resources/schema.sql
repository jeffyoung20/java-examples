CREATE SCHEMA IF NOT EXISTS  test;

DROP TABLE IF EXISTS person;

CREATE TABLE test.person (
  id bigint NOT NULL AUTO_INCREMENT,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ;
