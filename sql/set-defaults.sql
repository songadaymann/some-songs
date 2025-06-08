-- alter statements to set default values for columns
UPDATE MessageBoardPost SET numReplies = 0 WHERE numReplies IS NULL;
ALTER TABLE MessageBoardPost MODIFY numReplies INTEGER DEFAULT 0 NOT NULL;

UPDATE Playlist SET numItems = 0 WHERE numItems IS NULL;
ALTER TABLE Playlist MODIFY numItems INTEGER NOT NULL DEFAULT 0;

UPDATE PlaylistComment SET numReplies = 0 WHERE numReplies IS NULL;
ALTER TABLE PlaylistComment MODIFY numReplies INTEGER NOT NULL DEFAULT 0;

UPDATE Song SET numRatings = 0 WHERE numRatings IS NULL;
ALTER TABLE Song MODIFY numRatings INTEGER DEFAULT 0 NOT NULL;

UPDATE SongComment SET numReplies = 0 WHERE numReplies IS NULL;
ALTER TABLE SongComment MODIFY numReplies INTEGER NOT NULL DEFAULT 0;

UPDATE songRating SET disabled = 0 WHERE disabled IS NULL;
ALTER TABLE songRating MODIFY disabled BIT(1) NOT NULL DEFAULT 0;

UPDATE playlistRating SET disabled = 0 WHERE disabled IS NULL;
ALTER TABLE playlistRating MODIFY disabled BIT(1) NOT NULL DEFAULT 0;
