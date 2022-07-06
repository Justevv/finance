insert into users (id, password, username, is_email_confirmed, is_phone_confirmed, email, phone)
values (1, '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'sadmin', true, true, 'aa@d.ru', '4334');
insert into users (id, password, username, is_email_confirmed, is_phone_confirmed, email, phone)
values (2, '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'admin', true, true, 'ac@d.ru', '43341');
insert into users (id, password, username, is_email_confirmed, is_phone_confirmed, email, phone)
values (3, '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'user', true, true, 'ab@d.ru', '325');
insert into role
values (1, 'ROLE_SUPER_ADMIN');
insert into role
values (2, 'ROLE_ADMIN');
insert into role
values (3, 'ROLE_USER');
insert into roles_permissions
values (1, 'all:read');
insert into roles_permissions
values (1, 'all:delete');
insert into roles_permissions
values (1, 'all:write');
insert into roles_permissions
values (1, 'user:read');
insert into roles_permissions
values (1, 'user:delete');
insert into roles_permissions
values (1, 'user:write');
insert into roles_permissions
values (1, 'role:crud');
insert into roles_permissions
values (2, 'all:read');
insert into roles_permissions
values (2, 'all:delete');
insert into roles_permissions
values (2, 'all:write');
insert into roles_permissions
values (2, 'user:read');
insert into roles_permissions
values (2, 'user:delete');
insert into roles_permissions
values (2, 'user:write');
insert into roles_permissions
values (2, 'role:crud');
insert into roles_permissions
values (3, 'user:read');