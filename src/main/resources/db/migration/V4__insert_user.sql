insert into users (id, password, username, is_email_confirmed ,is_phone_confirmed, email, phone)
values (1, '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'sadmin', true, true, 'aa@d.ru', '4334');
insert into users (id, password, username, is_email_confirmed ,is_phone_confirmed, email, phone)
values (2, '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'admin', true, true, 'ac@d.ru', '43341');
insert into users (id, password, username, is_email_confirmed ,is_phone_confirmed, email, phone)
values (3, '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'user', true, true, 'ab@d.ru', '325');
insert into role
values (1, 'ROLE_SUPER_ADMIN');
insert into role
values (2, 'ROLE_ADMIN');
insert into role
values (3, 'ROLE_USER');
insert into permission
values (1, 'user:read');
insert into permission
values (2, 'user:delete');
insert into permission
values (3, 'user:write');
insert into permission
values (4, 'users:t');
insert into permission
values (5, 'role:crud');
insert into users_roles
values (1, 1);
insert into users_roles
values (2, 2);
insert into users_roles
values (3, 3);
insert into roles_permissions
values (1, 1);
insert into roles_permissions
values (1, 2);
insert into roles_permissions
values (1, 3);
insert into roles_permissions
values (1, 4);
insert into roles_permissions
values (1, 5);
insert into roles_permissions
values (2, 1);
insert into roles_permissions
values (2, 2);
insert into roles_permissions
values (2, 3);
insert into roles_permissions
values (2, 4);
insert into roles_permissions
values (2, 5);
insert into roles_permissions
values (3, 4);
