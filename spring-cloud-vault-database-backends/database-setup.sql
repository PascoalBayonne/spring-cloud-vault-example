--
-- Sample schema for testing vault database secrets
--
create schema vaultdb;
use vaultdb;
create table product
(
    id          varchar(55) not null,
    price       float       not null,
    description varchar(255)
);

--
-- MySQL user that will be used by Vault to create other users on demand
--
create user 'vaultdb-admin'@'%' identified by 'reddeadredemption#2';
grant all privileges on vaultdb.* to 'vaultdb-admin'@'%' with grant option;
grant create user on *.* to 'vaultdb-admin' with grant option;

flush privileges;