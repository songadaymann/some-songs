CREATE TABLE artist (

  artistId int not null auto_increment,

  userId int not null,

  name varchar(128) not null,
  info varchar(1024),

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(artistId)

);

CREATE TABLE relatedArtist (

  id int not null auto_increment,

  artistId int not null,
  relatedArtistId int not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(id)
);

CREATE TABLE artistOtherUser (

  id int not null auto_increment,

  artistId int not null,
  otherUserId int not null,

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(id)
);

CREATE TABLE artistLink (

  artistLinkId int not null auto_increment,

  artistId int not null,

  label varchar(128),
  url varchar(128) not null,
  notes varchar(256),

  changeDate timestamp not null default CURRENT_TIMESTAMP,
  createDate timestamp not null default '0000-00-00 00:00:00',

  primary key(artistLinkId)

);
