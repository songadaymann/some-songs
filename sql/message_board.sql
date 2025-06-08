CREATE TABLE MessageBoardTopic (

  topicId int not null auto_increment,

  name varchar(64) not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(topicId)

);

INSERT INTO MessageBoardTopic VALUES(1, 'General', NOW(), NOW());

INSERT INTO MessageBoardTopic VALUES(2, 'Songs/Artists', NOW(), NOW());

INSERT INTO MessageBoardTopic VALUES(3, 'Other Music', NOW(), NOW());

INSERT INTO MessageBoardTopic VALUES(4, 'Music Production', NOW(), NOW());

INSERT INTO MessageBoardTopic VALUES(5, 'Re: SomeSongs', NOW(), NOW());

CREATE TABLE MessageBoardPost (

  postId int not null auto_increment,

  userId int not null,

  topicId int not null,

  originalPostId int not null,

  subject varchar(64) not null,

  content varchar(4000) not null,
  moreContent varchar(4000),

  numReplies int not null default 0,

  lastReplyDate timestamp,

  locked number(1) not null default 0,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(postId)

);