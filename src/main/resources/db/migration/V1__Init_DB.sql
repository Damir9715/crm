create table post
(
  id      integer not null auto_increment,
  tag     varchar(255),
  text    varchar(255) not null,
  user_id integer,
  primary key (id)
) engine = InnoDB;

create table role
(
  id   integer not null auto_increment,
  name varchar(255),
  primary key (id)
) engine = InnoDB;

create table user
(
  id       integer not null auto_increment,
  password varchar(255) not null,
  username varchar(255) not null,
  primary key (id)
) engine = InnoDB;

create table user_role
(
  user_id integer not null,
  role_id integer not null,
  primary key (user_id, role_id)
) engine = InnoDB;

create table user_subscriptions
(
  subscriber_id integer not null,
  channel_id    integer not null,
  primary key (channel_id, subscriber_id)
) engine = InnoDB;

alter table role
  add constraint UK_epk9im9l9q67xmwi4hbed25do unique (name);

alter table post
  add constraint FK72mt33dhhs48hf9gcqrq4fxte
    foreign key (user_id) references user (id);

alter table user_role
  add constraint FKa68196081fvovjhkek5m97n3y
    foreign key (role_id) references role (id);

alter table user_role
  add constraint FK859n2jvi8ivhui0rl0esws6o
    foreign key (user_id) references user (id);

alter table user_subscriptions
  add constraint FKs9oa7i0xgbrqmkvgnv0r19m9v
    foreign key (channel_id) references user (id);

alter table user_subscriptions
  add constraint FKl9bhyxre3khiisorjq37vbr3f
    foreign key (subscriber_id) references user (id);

insert into role(name) values('ADMIN');
insert into role(name) values('TEACHER');
insert into role(name) values('STUDENT');
