package com.ssj.service.song;

import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.model.song.SongRating;
import com.ssj.model.base.Rating;
import com.ssj.dao.song.SongRatingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
@Transactional(readOnly = true)
public class SongRatingServiceImpl
implements   SongRatingService, ApplicationEventPublisherAware {

  private SongRatingDAO songRatingDAO;
  private SongService songService;
  private ApplicationEventPublisher eventPublisher;

  @Transactional(readOnly = false)
  public void rateSong(User user, Song song, String rating) {
    SongRating userRating = songRatingDAO.getRating(user, song);

    if (userRating == null) {
      userRating = new SongRating();

      userRating.setUser(user);
      userRating.setSong(song);
    }

    Integer ratingInt = Rating.getRatingFromLabel(rating);
    if (ratingInt != null) {
      userRating.setRating(ratingInt);
    }

    SongRatingEvent event = new SongRatingEvent(this, userRating);
    if (ratingInt == null) {
      songRatingDAO.delete(userRating);
      event.setActionValue(null);
    } else {
      songRatingDAO.save(userRating);
      event.setActionValue(Integer.toString(userRating.getRating()));
    }
    // trigger event to update song rating data, publish ogp action, etc.
    eventPublisher.publishEvent(event);
  }

  public SongRating getRating(User user, Song song) {
    return songRatingDAO.getRating(user, song);
  }

  public int countNonHiddenRatedSongs(User user) {
    return songRatingDAO.countNonHiddenRatedSongs(user);
  }

  public float getAverageSongRating(User user) {
    return songRatingDAO.getAverageSongRating(user);
  }

  /**
   * "When you are logged in, user info pages will show you a percentage indicating how much the user in question agrees with you.
   * This number is determined by comparing the ratings you've given on every song that both of you have rated.
   * These comparisons assign percentages like this:
   * good vs. bad : 0%
   * good or bad vs. okay : 50%
   * good vs. good, bad vs. bad, okay vs. okay : 100%
   * These comparisons are averaged together, rounded to the nearest 5% (to maintain some secrecy about specific ratings),
   * and that's the number you see.
   * ---Jefff 2003-09-18 12:15:07"
   * SQL:
   * select sr1.rating, sr2.rating
   * from songRating sr1, songRating sr2
   * where sr1.songId = sr2.songId
   * and sr1.userId = 1
   * and sr2.userId = 2
   * iterate over results, if ratings are equal, 1, otherwise if diff == 5, .5, otherwise 0
   * average all those 1/.5/0, round to nearest 5%
   *
   * @param user1 a user
   * @param user2 another user
   * @return the agreement in the ratings of the users for songs both users have rated,
   * or null if there aren't any songs rated by both users
   */
  public Integer getRatingAgreement(User user1, User user2) {
    // return null if there are no songs that both users have rated
    Integer agreement = null;

    List ratings = songRatingDAO.getRatingsForSameSongs(user1, user2);

    if (ratings != null && !ratings.isEmpty()) {
      float agreementPercentage = 0;

      for (Object rating : ratings) {
        Object[] ratingsForSong = (Object[]) rating;
        Integer rating1 = (Integer) ratingsForSong[0];
        Integer rating2 = (Integer) ratingsForSong[1];

        if (rating1.equals(rating2)) {
          // total agreement = 100% agreement
          agreementPercentage += 100;
        } else if (Math.abs(rating1 - rating2) == 5) {
          // difference of one rating level = 50% agreement
          agreementPercentage += 50;
        }
        // otherwise 0% agreement
      }

      // calculate the average
      agreementPercentage /= ratings.size();

      // round to the nearest 5.0
      agreement = Math.round(agreementPercentage / 5) * 5;
    }

    return agreement;
  }

  public List<SongRating> getRecentRatingsWithSongs(User user, int start, int pageSize) {
    if (!user.isAdmin()) {
      throw new SongRatingException("User " + user.getDisplayName() + "is not an administrator");
    }
    return songRatingDAO.getRecentRatingsWithSongs(start, pageSize);
  }

  @Transactional(readOnly = false)
  public SongRating toggleDisabled(User user, int ratingId) {
    if (user.isAdmin()) {
      SongRating rating = songRatingDAO.get(ratingId);
      if (rating != null) {
        rating.setDisabled(!rating.isDisabled());
        rating.setChangeDate(new Date());
        songRatingDAO.save(rating);

        Song song = songService.getSong(rating.getSong().getId());
        songService.updateRatingInfo(song);
        return rating;
      } else {
        throw new SongRatingException("No rating with id " + ratingId);
      }
    } else {
      throw new SongRatingException("User " + user.getDisplayName() + " is not an administrator");
    }
  }

  public List<SongRating> getRatings(User user, List<Song> songs) {
    return songRatingDAO.getRatings(user, songs);
  }

  @Autowired
  public void setSongRatingDAO(SongRatingDAO songRatingDAO) {
    this.songRatingDAO = songRatingDAO;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Override
  public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
    eventPublisher = applicationEventPublisher;
  }
}
