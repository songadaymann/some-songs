CREATE TABLE song (

  songId int not null auto_increment,

  showSong integer not null default 1,

  title varchar(256) not null,
  url varchar(256) not null,
  bandcampUrl varchar(1024),
  info varchar(1024) not null,
  moreInfo varchar(1024),

  artistId int not null,

  numRatings int not null default 0;
  rating double,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(songId)

);

CREATE TABLE songComment (

  songCommentId int not null auto_increment,

  userId int not null,
  songId int not null,

  commentText varchar(512) not null,
  moreCommentText varchar(1024),

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(songCommentId)

);

CREATE TABLE songCommentReply (

  songCommentReplyId int not null auto_increment,

  userId int not null,
  songCommentId int not null,

  commentText varchar(512) not null,
  moreCommentText varchar(1024),

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(songCommentReplyId)

);

CREATE TABLE songRating (

  songRatingId int not null auto_increment,

  userId int not null,
  songId int not null,

  rating int not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(songRatingId)

);
