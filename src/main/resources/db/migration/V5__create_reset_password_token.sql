create table password_reset_token
(
    id        uuid not null,
    expire_time timestamp,
    token       varchar(255),
    user_id   uuid,
    primary key (id)
);

alter table if exists password_reset_token add constraint FK_password_reset_token_users foreign key (user_id) references users;