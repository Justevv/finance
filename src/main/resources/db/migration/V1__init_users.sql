create table role
(
    id uuid not null,
    name varchar(255),
    primary key (id)
);

create table roles_permissions
(
    role_id uuid not null,
    permissions varchar(255)
);

create table users_roles
(
    user_id uuid not null,
    role_id uuid not null,
    primary key (user_id, role_id)
);

create table users
(
    id                 uuid         not null,
    email              varchar(255) not null,
    is_email_confirmed boolean      not null,
    is_phone_confirmed boolean      not null,
    password           varchar(255) not null,
    phone              varchar(255) not null,
    username           varchar(255) not null,
    primary key (id)
);

create table phone_verification
(
    id          uuid not null,
    code        varchar(255),
    expire_time timestamp,
    user_id   uuid,
    is_sent boolean not null default false,
    primary key (id)
);

create table email_verification
(
    id          uuid not null,
    code        varchar(255),
    expire_time timestamp,
    user_id   uuid,
    is_sent boolean not null default false,
    primary key (id)
);

alter table if exists phone_verification add constraint FK_phone_verification_users foreign key (user_id) references users on delete cascade;
alter table if exists email_verification add constraint FK_email_verification_users foreign key (user_id) references users on delete cascade;
alter table if exists role add constraint UK_role_name unique (name);
alter table if exists users add constraint UK_users_username unique (username);
alter table if exists roles_permissions add constraint FK_roles_permissions_role foreign key (role_id) references role;
alter table if exists users_roles add constraint FK_users_roles_role foreign key (role_id) references role;
alter table if exists users_roles add constraint FK_users_roles_users foreign key (user_id) references users;