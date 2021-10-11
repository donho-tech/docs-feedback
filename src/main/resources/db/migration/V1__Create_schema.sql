create table users
(
    id   integer primary key generated always as identity,
    name varchar(100)
);