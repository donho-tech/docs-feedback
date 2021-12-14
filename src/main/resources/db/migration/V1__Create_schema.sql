create table users
(
    id   integer primary key generated always as identity,
    name varchar(100)
);

create table documentation
(
    id   integer primary key generated always as identity,
    name varchar(100)
);

create table rating
(
    id           integer primary key generated always as identity,
--     date         timestamp with time zone,
    helpful      boolean not null,
    comment      text,
    reference_id varchar(300),
    link         varchar(300),
    docs_id      integer,
    constraint fk_documentation foreign key (docs_id) references documentation (id)
);