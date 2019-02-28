# dev ADMIN id = 1
insert into user (id, password, username)
  VALUES (1, '$2a$10$k3ONX/R4DTFkO1eU3AwkPe6Ao06h.GtCXQeHDWPVINily5RDeBzGu', 'dev');

insert into user_role (user_id, role_id)
  VALUES (1, 1);

# john TEACHER id = 2
insert into user (id, password, username)
VALUES (2, '$2a$10$k3ONX/R4DTFkO1eU3AwkPe6Ao06h.GtCXQeHDWPVINily5RDeBzGu', 'john');

insert into user_role (user_id, role_id)
VALUES (2, 2);

# artem STUDENT id = 3
insert into user (id, password, username)
VALUES (3, '$2a$10$k3ONX/R4DTFkO1eU3AwkPe6Ao06h.GtCXQeHDWPVINily5RDeBzGu', 'artem');

insert into user_role (user_id, role_id)
VALUES (3, 3);