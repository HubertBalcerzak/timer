create table sessions
(
    id        serial primary key,
    userId    bigint    not null,
    day       date      not null,
    createdAt timestamp not null,
    constraint fk_sessions_users foreign key (userId) references users (id)
);

alter table events
    add sessionId bigint not null,
    add constraint fk_events_sessions foreign key (sessionId) references sessions (id)