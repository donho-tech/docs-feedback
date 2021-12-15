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

create table document
(
    id               integer primary key generated always as identity,
    reference_id     varchar(300),
    link             varchar(300),
    documentation_id integer,
    constraint fk_documentation foreign key (documentation_id) references documentation (id)
);

create table rating
(
    id          integer primary key generated always as identity,
    datetime    timestamp with time zone,
    helpful     boolean not null,
    comment     text,
    document_id integer,
    constraint fk_document foreign key (document_id) references document (id)
);