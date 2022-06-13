create sequence hibernate_sequence start 3 increment 1;
create table account
(
    id      int8 not null,
    name    varchar(255),
    user_id int8,
    primary key (id)
);

create table role
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
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

create table users_roles
(
    user_id int8 not null,
    role_id int8 not null
);

alter table if exists users add constraint UK_users_username unique (username);
alter table if exists users add constraint UK_users_email unique (email);
alter table if exists users add constraint UK_users_phone unique (phone);
alter table if exists account add constraint FKra7xoi9wtlcq07tmoxxe5jrh4 foreign key (user_id) references users;
alter table if exists users_roles add constraint FKt4v0rrweyk393bdgt107vdx0x foreign key (role_id) references role;
alter table if exists users_roles add constraint FK2o0jvgh89lemvvo17cbqvdxaa foreign key (user_id) references users;