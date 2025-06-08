CREATE TABLE user (

  userId int not null auto_increment,

  username varchar(64) not null,
  password varchar(32) not null,
  displayName varchar(64) not null,
  email varchar(128) not null,

  showEmailInUserInfo int default 0 not null,

  websiteName varchar(64),
  websiteURL varchar(128),
  location varchar(64) not null,

  goodBand varchar(64),
  goodAlbum varchar(64),
  goodSong varchar(64),
  goodBook varchar(64),
  goodMovie varchar(64),
  goodWebsiteName varchar(64),
  goodWebsiteURL varchar(128),

  admin int default 0 not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(userId)

);

CREATE TABLE favoriteSong (

  id int not null auto_increment,

  userId int not null,
  favoriteSongId int not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(id)
);

CREATE TABLE favoriteArtist (

  id int not null auto_increment,

  userId int not null,
  favoriteArtistId int not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(id)
);

CREATE TABLE preferredUser (

  id int not null auto_increment,

  userId int not null,
  preferredUserId int not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(id)
);

CREATE TABLE ignoredUser (

  id int not null auto_increment,

  userId int not null,
  ignoredUserId int not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(id)
);

