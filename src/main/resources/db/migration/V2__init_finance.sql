create table account
(
    id      int8 not null,
    name    varchar(255),
    user_id int8,
    primary key (id)
);

create table category
(
    id                 int8 not null,
    name               varchar(255),
    parent_category_id int8,
    user_id            int8,
    primary key (id)
);

create table expense
(
    id               int8   not null,
    date             timestamp,
    description      varchar(255),
    sum              float8 not null,
    transaction_type int4,
    account_id       int8,
    category_id      int8,
    payment_type_id  int8,
    place_id         int8,
    user_id          int8,
    primary key (id)
);

create table payment_type
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);

create table place
(
    id      int8 not null,
    address varchar(255),
    name    varchar(255),
    user_id int8,
    primary key (id)
);


alter table if exists category add constraint FK_category_category foreign key (parent_category_id) references category;
alter table if exists expense add constraint FK_expense_account foreign key (account_id) references account;
alter table if exists expense add constraint FK_expense_category foreign key (category_id) references category;
alter table if exists expense add constraint FK_expense_payment_type foreign key (payment_type_id) references payment_type;
alter table if exists expense add constraint FK_expense_users foreign key (user_id) references users;