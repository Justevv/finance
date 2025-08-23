create table role
(
    guid uuid not null,
    name varchar(255),
    primary key (guid)
);

create table roles_permissions
(
    role_guid uuid not null,
    permissions varchar(255)
);

create table users_roles
(
    user_guid uuid not null,
    role_guid uuid not null,
    primary key (user_guid, role_guid)
);

create table users
(
    guid               uuid         not null,
    email              varchar(255) not null,
    is_email_confirmed boolean      not null,
    is_phone_confirmed boolean      not null,
    password           varchar(255) not null,
    phone              varchar(255) not null,
    username           varchar(255) not null,
    primary key (guid)
);

create table verification
(
    guid        uuid not null,
    code        varchar(255),
    expire_time timestamp,
    type        int4,
    user_guid   uuid,
    primary key (guid)
);

alter table if exists verification add constraint FK_verification_users foreign key (user_guid) references users on delete cascade;
alter table if exists role add constraint UK_role_name unique (name);
alter table if exists users add constraint UK_users_username unique (username);
alter table if exists roles_permissions add constraint FK_roles_permissions_role foreign key (role_guid) references role;
alter table if exists users_roles add constraint FK_users_roles_role foreign key (role_guid) references role;
alter table if exists users_roles add constraint FK_users_roles_users foreign key (user_guid) references users;