create table if not exists user_list
(
    id       bigserial primary key,
    name     varchar(255) not null,
    username varchar(255) not null unique,
    password varchar(255) not null
);

create table if not exists task_list
(
    id              bigserial primary key,
    title           varchar(255) not null,
    description     varchar(255) null,
    status          varchar(255) not null,
    expiration_date timestamp    null
);

create table if not exists user_task
(
    user_id bigint not null,
    task_id bigint not null,
    primary key (user_id, task_id),
    constraint fk_user_task_user_list foreign key (user_id) references user_list (id) on delete cascade on update no action,
    constraint fk_user_task_task_list foreign key (task_id) references task_list (id) on delete cascade on update no action
);

create table if not exists user_roles
(
    user_id bigint       not null,
    role    varchar(255) not null,
    primary key (user_id, role),
    constraint fk_user_roles_user_list foreign key (user_id) references user_list (id) on delete cascade on update no action
);