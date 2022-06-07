create table counter (id int4 not null, date timestamp, paid float8 not null, price float8 not null, value float8 not null, primary key (id));
create table electricity_counter (id int4 not null, date timestamp, paid float8 not null, price float8 not null, value float8 not null, primary key (id));
create table gas_counter (id int4 not null, date timestamp, paid float8 not null, price float8 not null, value float8 not null, primary key (id));
create table water_counter (id int4 not null, date timestamp, paid float8 not null, price float8 not null, value float8 not null, primary key (id));