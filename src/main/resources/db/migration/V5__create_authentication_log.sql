create table authentication_log
(
    id              int8 not null,
    date_time       timestamp,
    ip_address      varchar(255),
    username        varchar(255),
    user_agent       bytea,
    primary key (id)
);
