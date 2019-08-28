insert into user (id, username, password, active)
    values (1, 'agrmv', 'admin', true);

insert into user_role (user_id, roles)
    values (1, 'ROLE_USER'), (1, 'ROLE_ADMIN');
