insert into users (id, password, username, is_email_confirmed, is_phone_confirmed, email, phone)
values ('72cf136e-3142-4ffb-b293-a8eaaa8c4c44', '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'sadmin', true, true, 'aa@d.ru', '4334');
insert into users (id, password, username, is_email_confirmed, is_phone_confirmed, email, phone)
values ('72cf136e-3142-4ffb-b293-a8eaaa8c4c45', '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'admin', true, true, 'ac@d.ru', '43341');
insert into users (id, password, username, is_email_confirmed, is_phone_confirmed, email, phone)
values ('72cf136e-3142-4ffb-b293-a8eaaa8c4c46', '$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty', 'user', true, true, 'ab@d.ru', '325');
insert into role
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'ROLE_SUPER_ADMIN');
insert into role
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'ROLE_ADMIN');
insert into role
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c46', 'ROLE_USER');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'all:read');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'all:delete');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'all:write');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'user:read');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'user:delete');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'user:write');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c44', 'role:crud');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'all:read');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'all:delete');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'all:write');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'user:read');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'user:delete');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'user:write');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c45', 'role:crud');
insert into roles_permissions
values ('72cf136e-3142-4ffb-b293-b8eaaa8c4c46', 'user:read');