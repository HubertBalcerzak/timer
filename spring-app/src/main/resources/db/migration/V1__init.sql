create table tasks
(
    id        serial primary key,
    name      varchar(128) not null,
    createdAt timestamp default current_timestamp
);
