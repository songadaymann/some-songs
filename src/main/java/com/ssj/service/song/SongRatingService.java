package com.ssj.service.song;

import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.model.song.SongRating;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SongRatingService {

  public void rateSong(User user, Song song, String rating);

  public List<SongRating> getRatings(User user, List<Song> songs);

  public SongRating getRating(User user, Song song);

  public int countNonHiddenRatedSongs(User user);

  public float getAverageSongRating(User user);

  public Integer getRatingAgreement(User user1, User user2);

  public List<SongRating> getRecentRatingsWithSongs(User user, int start, int pageSize);

  public SongRating toggleDisabled(User user, int ratingId);
}
