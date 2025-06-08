CREATE TABLE PageContent (
  pageContentId int not null auto_increment,

  type int not null,

  content varchar(8192) not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(pageContentId)
);
