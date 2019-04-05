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

create table schedule
(
  id          int4 not null,
  day         varchar(255),
  description varchar(255),
  room        varchar(255),
  time        varchar(255),
  user_id     int4,
  primary key (id)
);

create table share
(
  post_id int4 not null,
  user_id int4 not null,
  primary key (post_id, user_id)
);

create table share_schedule
(
  schedule_id int4 not null,
  user_id     int4 not null,
  primary key (schedule_id, user_id)
);

create table subject
(
  id           int4 not null,
  subject_name varchar(255),
  primary key (id)
);

create table subject_user
(
  subject_id int4 not null,
  user_id    int4 not null,
  primary key (subject_id, user_id)
);

create table user_role
(
  user_id int4 not null,
  role_id int4 not null,
  primary key (user_id, role_id)
);

create table user_subject
(
  user_id    int4 not null,
  subject_id int4 not null,
  primary key (user_id, subject_id)
);

create table user_subscriptions
(
  subscriber_id int4 not null,
  channel_id    int4 not null,
  primary key (channel_id, subscriber_id)
);

create table usr
(
  id        int4    not null,
  active    boolean not null,
  password  varchar(255),
  username  varchar(255),
  firstname varchar(255),
  surname   varchar(255),
  phone     varchar(255),
  age       int4,
  primary key (id)
);


alter table if exists role
  drop constraint if exists UK_epk9im9l9q67xmwi4hbed25do;
alter table if exists role
  add constraint UK_epk9im9l9q67xmwi4hbed25do unique (name);
alter table if exists post
  add constraint FKrm2u0ujvvi9euawhsm1km29m4 foreign key (user_id) references usr;
alter table if exists schedule
  add constraint FKnhsv1xmxy1ywftyiq79917sni foreign key (user_id) references usr;
alter table if exists share
  add constraint FK5ahgy1j7jabu3pkr2ojcvu628 foreign key (user_id) references usr;
alter table if exists share
  add constraint FKjvqve4c7t902i00bw4q6bm36r foreign key (post_id) references post;
alter table if exists share_schedule
  add constraint FK7kf2rq28d6f50ypg42ffxi1ch foreign key (user_id) references usr;
alter table if exists share_schedule
  add constraint FK5oqjhcernxjeq7innrj98n730 foreign key (schedule_id) references schedule;
alter table if exists subject_user
  add constraint FK1pss54nydmaksmbjgg9o5xmn5 foreign key (user_id) references usr;
alter table if exists subject_user
  add constraint FK2nwoey17n8mi4hyetyb6xvy8q foreign key (subject_id) references subject;
alter table if exists user_role
  add constraint FKa68196081fvovjhkek5m97n3y foreign key (role_id) references role;
alter table if exists user_role
  add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr;
alter table if exists user_subject
  add constraint FK8xwdo7i5lavkapi3hf891raea foreign key (subject_id) references subject;
alter table if exists user_subject
  add constraint FK5uv64pya76p64ny3o845b7wp9 foreign key (user_id) references usr;
alter table if exists user_subscriptions
  add constraint FK74b7d4i0rhhj8jljvidimewie foreign key (channel_id) references usr;
alter table if exists user_subscriptions
  add constraint FKm69uaasbua17sgdnhsq10yxd5 foreign key (subscriber_id) references usr;


insert into role(id, name)
values (1, 'ADMIN');
insert into role(id, name)
values (2, 'TEACHER');
insert into role(id, name)
values (3, 'STUDENT');

insert into subject(id, subject_name)
values (1, 'C++');
insert into subject(id, subject_name)
values (2, 'Python');
insert into subject(id, subject_name)
values (3, 'Photoshop');
insert into subject(id, subject_name)
values (4, 'Java');
insert into subject(id, subject_name)
values (5, 'AutoCAD');
insert into subject(id, subject_name)
values (6, '3DsMax');