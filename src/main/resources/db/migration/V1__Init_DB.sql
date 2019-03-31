create sequence hibernate_sequence start 1 increment 1;

create table post
(
id      int4 not null,
tag     varchar(255),
text    varchar(255),
user_id int4,
primary key (id)
);

create table role
(
id   int4 not null,
name varchar(255),
primary key (id)
);

create table user_role
(
user_id int4 not null,
role_id int4 not null,
primary key (user_id, role_id)
);

create table user_subscriptions
(
subscriber_id int4 not null,
channel_id    int4 not null,
primary key (channel_id, subscriber_id)
);

create table usr
(
id       int4 not null,
password varchar(255),
username varchar(255),
primary key (id)
);

alter table if exists role
drop constraint if exists UK_epk9im9l9q67xmwi4hbed25do;

alter table if exists role
add constraint UK_epk9im9l9q67xmwi4hbed25do unique (name);


alter table if exists post
  add constraint FKrm2u0ujvvi9euawhsm1km29m4
    foreign key (user_id) references usr;

alter table if exists user_role
  add constraint FKa68196081fvovjhkek5m97n3y
    foreign key (role_id) references role;

alter table if exists user_role
  add constraint FKfpm8swft53ulq2hl11yplpr5
    foreign key (user_id) references usr;

alter table if exists user_subscriptions
  add constraint FK74b7d4i0rhhj8jljvidimewie
    foreign key (channel_id) references usr;

alter table if exists user_subscriptions
  add constraint FKm69uaasbua17sgdnhsq10yxd5
    foreign key (subscriber_id) references usr;


insert into role(id, name) values (1, 'ADMIN');
insert into role(id, name) values (2, 'TEACHER');
insert into role(id, name) values (3, 'STUDENT');

insert into day(id, day_name) values(1, 'Monday');
insert into day(id, day_name) values(2, 'Tuesday');
insert into day(id, day_name) values(3, 'Wednesday');
insert into day(id, day_name) values(4, 'Thursday');
insert into day(id, day_name) values(5, 'Friday');

insert into time(id, time_name) values(1, '13-14');
insert into time(id, time_name) values(2, '14-15');
insert into time(id, time_name) values(3, '15-16');
insert into time(id, time_name) values(4, '16-17');
insert into time(id, time_name) values(5, '17-18');
insert into time(id, time_name) values(6, '18-19');

insert into room(id, room_name) values(1, '601');
insert into room(id, room_name) values(2, '604');
insert into room(id, room_name) values(3, '610');
insert into room(id, room_name) values(4, '802');
insert into room(id, room_name) values(5, '501');

insert into subject(id, subject_name) values(1, 'Database');
insert into subject(id, subject_name) values(2, 'CSA');
insert into subject(id, subject_name) values(3, 'Web');
insert into subject(id, subject_name) values(4, 'Java');

-- insert into grp(id, group_name) values(1, 'is04');
-- insert into grp(id, group_name) values(2, 'is03');
-- insert into grp(id, group_name) values(3, 'is02');

insert into schedule(id, schedule_name, day_id, room_id, subject_id, user_id, time_id, group_id) values(1, 'm1', 1, 1, 1, 1, 1, 1);
