package com.ssj.dao.song;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.Song;
import com.ssj.dao.SearchDAO;

public interface SongDAO extends SearchDAO<Song, SongSearch> {

  Song getNewerSong(int songId);

  Song getOlderSong(int songId);

  Song getHigherRatedSong(int songId);

  Song getLowerRatedSong(int songId);

  float getAverageSongRating();

  float getAverageNumRatings();

  void updateRatingInfo(Song song);

  Song findByBandcmapTrackId(long trackId);
}
