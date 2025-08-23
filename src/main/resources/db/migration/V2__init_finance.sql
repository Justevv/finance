create table account
(
    guid    uuid not null,
    name    varchar(255),
    user_guid uuid,
    primary key (guid)
);

create table category
(
    guid               uuid not null,
    name               varchar(255),
    parent_category_guid uuid,
    user_guid            uuid,
    primary key (guid)
);

create table expense
(
    guid uuid not null,
    date timestamp,
    description varchar(255),
    sum float8 not null,
    transaction_type int4,
    account_guid uuid,
    category_guid uuid,
    payment_type_guid uuid,
    place_guid uuid,
    user_guid uuid,
    primary key (guid)
);

create table payment_type
(
    guid    uuid not null,
    name    varchar(255),
    user_guid uuid,
    primary key (guid)
);

create table place
(
    guid    uuid not null,
    address varchar(255),
    name    varchar(255),
    user_guid uuid,
    primary key (guid)
);


alter table if exists category add constraint FK_category_category foreign key (parent_category_guid) references category;
alter table if exists category add constraint FK_category_users foreign key (user_guid) references users;
alter table if exists expense add constraint FK_expense_account foreign key (account_guid) references account;
alter table if exists expense add constraint FK_expense_category foreign key (category_guid) references category;
alter table if exists expense add constraint FK_expense_payment_type foreign key (payment_type_guid) references payment_type;
alter table if exists expense add constraint FK_expense_users foreign key (user_guid) references users;
alter table if exists expense add constraint FK_expense_place foreign key (place_guid) references place;
alter table if exists account add constraint FK_account_users foreign key (user_guid) references users;
alter table if exists payment_type add constraint FK_payment_type_users foreign key (user_guid) references users;
alter table if exists place add constraint FK_place_users foreign key (user_guid) references users;