create table bj_post
(
   id                   int not null auto_increment,
   url                  varchar(500) not null default '',
   title                varchar(500) not null,
   content              longtext not null,
   addtime              datetime not null,
   top                  int default 0,
   status               varchar(20) not null default 'public',
   type                 varchar(20) not null default 'post',
   primary key (id)
);

create table bj_post_tag
(
   post_id              int,
   tag_id               int
);

create table bj_setting
(
   name                 varchar(100) not null,
   description          varchar(200),
   value                longtext not null,
   primary key (name)
);

create table bj_tag
(
   id                   int not null auto_increment,
   name                 varchar(50) not null,
   sort                 int default 0,
   post_count           int default 0,
   primary key (id)
);

alter table bj_post_tag add constraint FK_TO_POST foreign key (post_id)
      references bj_post (id) on delete restrict on update restrict;

alter table bj_post_tag add constraint FK_TO_TAG foreign key (tag_id)
      references bj_tag (id) on delete restrict on update restrict;

