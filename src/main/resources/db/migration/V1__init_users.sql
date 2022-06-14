create sequence hibernate_sequence start 3 increment 1;
create table account
(
    id      int8 not null,
    name    varchar(255),
    user_id int8,
    primary key (id)
);

create table permission
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);

create table role
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);

create table roles_permissions
(
    role_id        int8 not null,
    permissions_id int8 not null
);

create table users_roles
(
    user_id int8 not null,
    role_id int8 not null
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

alter table if exists permission add constraint UK_2ojme20jpga3r4r79tdso17gi unique (name);
alter table if exists role add constraint UK_8sewwnpamngi6b1dwaa88askk unique (name);
alter table if exists users add constraint UK_users_username unique (username);
alter table if exists account add constraint FKra7xoi9wtlcq07tmoxxe5jrh4 foreign key (user_id) references users;
alter table if exists roles_permissions add constraint FKsidab0lpqi82o4o15bwde2c5f foreign key (permissions_id) references permission;
alter table if exists roles_permissions add constraint FKa6jx8n8xkesmjmv6jqug6bg68 foreign key (role_id) references role;
alter table if exists users_roles add constraint FKa68196081fvovjhkek5m97n3y foreign key (role_id) references role;
alter table if exists users_roles add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users;