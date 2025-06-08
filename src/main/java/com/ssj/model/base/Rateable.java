package com.ssj.model.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@MappedSuperclass
public abstract class Rateable {

  public static final DecimalFormat RATING_FORMAT = new DecimalFormat("#0.00");

  public static final int MIN_RATINGS = 5; //number of ratings required before average rating is public

  @Column(nullable = false)
  private int numRatings;
  private Float rating;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastRated;

  public int getNumRatings() {
//    return (ratings.size());
    return numRatings;
  }

  public void setNumRatings(int numRatings) {
    this.numRatings = numRatings;
  }

  public boolean showRating() {
    return getNumRatings() >= MIN_RATINGS;
  }

  public boolean isShowRating() {
    return showRating();
  }

  public int getNumRatingsNeeded() {
    return Math.max(MIN_RATINGS - getNumRatings(), 0);
  }

  public Float getRating() {
    return (rating == null ? 0f : rating);
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public String getRatingString() {
    return RATING_FORMAT.format(getRating());
  }

  public Date getLastRated() {
    return lastRated;
  }

  public void setLastRated(Date lastRated) {
    this.lastRated = lastRated;
  }
}
