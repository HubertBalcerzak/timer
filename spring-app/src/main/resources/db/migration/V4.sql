create table events
(
    id             serial primary key,
    taskId         bigint    not null,
    day            date      not null,
    startTimestamp timestamp not null,
    endTimestamp   timestamp,
    constraint fk_events_tasks foreign key (taskId) references tasks (id)
)