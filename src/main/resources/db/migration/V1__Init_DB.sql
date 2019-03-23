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
