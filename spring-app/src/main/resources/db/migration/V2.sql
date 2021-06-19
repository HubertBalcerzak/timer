create table users
(
    id        serial primary key,
    subject   varchar(128) unique,
    createdAt timestamp default current_timestamp
);

alter table tasks
    add column userId bigint not null;

alter table tasks
    add constraint fk_tasks_users foreign key (userId) references users (id)
