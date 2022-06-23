create table password_reset_token
(
    id          int8 not null,
    expire_time timestamp,
    token       varchar(255),
    user_id     int8,
    primary key (id)
);

alter table if exists password_reset_token add constraint FK_password_reset_token_users foreign key (user_id) references users;