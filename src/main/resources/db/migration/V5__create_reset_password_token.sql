create table password_reset_token
(
    guid        uuid not null,
    expire_time timestamp,
    token       varchar(255),
    user_guid   uuid,
    primary key (guid)
);

alter table if exists password_reset_token add constraint FK_password_reset_token_users foreign key (user_guid) references users;