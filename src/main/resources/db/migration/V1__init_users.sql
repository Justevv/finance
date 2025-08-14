create table role
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);

create table roles_permissions
(
    role_id     int8 not null,
    permissions varchar(255)
);

create table users_roles
(
    user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);

create table users
(
    id                 int8         not null,
    email              varchar(255) not null,
    is_email_confirmed boolean      not null,
    is_phone_confirmed boolean      not null,
    password           varchar(255) not null,
    phone              varchar(255) not null,
    username           varchar(255) not null,
    balance            numeric(19, 2) default 0,
    primary key (id)
);

create table verification
(
    id          int8 not null,
    code        varchar(255),
    expire_time timestamp,
    type        int4,
    user_id     int8,
    primary key (id)
);

alter table if exists verification add constraint FK_verification_users foreign key (user_id) references users on delete cascade;
alter table if exists role add constraint UK_role_name unique (name);
alter table if exists users add constraint UK_users_username unique (username);
alter table if exists roles_permissions add constraint FK_roles_permissions_role foreign key (role_id) references role;
alter table if exists users_roles add constraint FK_users_roles_role foreign key (role_id) references role;
alter table if exists users_roles add constraint FK_users_roles_users foreign key (user_id) references users;