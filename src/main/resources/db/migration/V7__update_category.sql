ALTER TABLE category DROP CONSTRAINT IF EXISTS fk_category_users;
ALTER TABLE category DROP COLUMN IF EXISTS user_guid;

create table favorite_category
(
    guid uuid,
    category_guid uuid not null,
    user_guid uuid not null,
    primary key (guid)
);


alter table if exists favorite_category add constraint FK_favorite_category_category_guid foreign key (category_guid) references category;
alter table if exists favorite_category add constraint FK_favorite_category_user_guid foreign key (user_guid) references users;