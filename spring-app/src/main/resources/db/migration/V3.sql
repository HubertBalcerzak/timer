create table day_tasks
(
    id       serial primary key,
    taskId   bigint    not null,
    day      date      not null,
    createdAt timestamp not null default current_timestamp,
    constraint fk_day_tasks__tasks foreign key (taskId) references tasks (id),
    constraint uq_taskId_day unique (taskId, day)
)
