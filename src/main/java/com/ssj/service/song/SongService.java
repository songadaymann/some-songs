package com.ssj.service.song;

import com.ssj.model.song.BrokenLinkReport;
import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.Song;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SongService {

  List<Song> findSongs(SongSearch search);

  int countSongs(SongSearch search);

  void save(Song song);

  Song getSong(int songId);

  Song getRandomSong();

  Song getNewerSong(int songId);

  Song getOlderSong(int songId);

  Song getHigherRatedSong(int songId);

  Song getLowerRatedSong(int songId);

  float getAverageSongRating();

  float getAverageNumRatings();

  void deleteSong(Song song);

  void updateRatingInfo(Song song);

  void checkSongLinks();

  void reportBrokenLink(Song song);

  List<BrokenLinkReport> getNewBrokenLinkReports(int start, int pageSize);

  BrokenLinkReport getBrokenLinkReport(int reportId);

  void saveBrokenLinkReport(BrokenLinkReport report);

  Song findSongByBandcampTrackId(long trackId);
}
