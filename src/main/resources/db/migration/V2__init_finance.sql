create table account
(
    id    uuid not null,
    name    varchar(255),
    user_id uuid,
    primary key (id)
);

create table category
(
    id uuid not null,
    name  varchar(255),
    parent_category_id uuid,
    primary key (id)
);

create table favorite_category
(
    id uuid,
    category_id uuid not null,
    user_id uuid not null,
    primary key (id)
);

create table expense
(
    id uuid not null,
    date timestamp,
    description varchar(255),
    amount decimal not null,
    transaction_type int4,
    account_id uuid,
    category_id uuid,
    payment_type_id uuid,
    place_id uuid,
    user_id uuid,
    primary key (id)
);

create table payment_type
(
    id    uuid not null,
    name    varchar(255),
    user_id uuid,
    primary key (id)
);

create table place
(
    id    uuid not null,
    address varchar(255),
    name    varchar(255),
    primary key (id)
);


alter table if exists category add constraint FK_category_category foreign key (parent_category_id) references category;
alter table if exists expense add constraint FK_expense_account foreign key (account_id) references account;
alter table if exists expense add constraint FK_expense_category foreign key (category_id) references category;
alter table if exists expense add constraint FK_expense_payment_type foreign key (payment_type_id) references payment_type;
alter table if exists expense add constraint FK_expense_users foreign key (user_id) references users;
alter table if exists expense add constraint FK_expense_place foreign key (place_id) references place;
alter table if exists account add constraint FK_account_users foreign key (user_id) references users;
alter table if exists payment_type add constraint FK_payment_type_users foreign key (user_id) references users;
alter table if exists favorite_category add constraint FK_favorite_category_category_id foreign key (category_id) references category;
alter table if exists favorite_category add constraint FK_favorite_category_user_id foreign key (user_id) references users;