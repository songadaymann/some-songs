package com.ssj.dao.song;

import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.model.song.SongRating;
import com.ssj.dao.DAO;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SongRatingDAO extends DAO<SongRating> {

  public List<SongRating> getRatings(User user, List<Song> songs);

  public SongRating getRating(User user, Song song);

  public int countNonHiddenRatedSongs(User user);

  public float getAverageSongRating(User user);

  public List getRatingsForSameSongs(User user1, User user2);

  public List<SongRating> getRecentRatingsWithSongs(int start, int pageSize);
}
