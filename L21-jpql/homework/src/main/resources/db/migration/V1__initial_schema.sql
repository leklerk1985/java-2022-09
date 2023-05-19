-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
create table client
(
    id bigserial not null primary key,
    name varchar(50)
);

create table address
(
    id bigserial not null primary key,
    street varchar(100),
    client_id bigserial,
    FOREIGN KEY(client_id) REFERENCES client(id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigserial,
    FOREIGN KEY(client_id) REFERENCES client(id)
);