--  dev ADMIN id = 1
insert into usr (id, password, username, active, firstname, surname, phone, age)
VALUES (100000000, '$2a$10$k3ONX/R4DTFkO1eU3AwkPe6Ao06h.GtCXQeHDWPVINily5RDeBzGu', 'dev', true, 'Devon', 'Larrat', '88008008080', 45);
insert into user_role (user_id, role_id) VALUES (100000000, 1);

-- --  john TEACHER id = 2
-- insert into usr (id, password, username)
-- VALUES (2, '$2a$10$k3ONX/R4DTFkO1eU3AwkPe6Ao06h.GtCXQeHDWPVINily5RDeBzGu', 'john');
--
-- insert into user_role (user_id, role_id)
-- VALUES (2, 2);
--
-- --  artem STUDENT id = 3
-- insert into usr (id, password, username)
-- VALUES (3, '$2a$10$k3ONX/R4DTFkO1eU3AwkPe6Ao06h.GtCXQeHDWPVINily5RDeBzGu', 'artem');
--
-- insert into user_role (user_id, role_id)
-- VALUES (3, 3);