package com.ssj.model.base;

import com.ssj.model.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@MappedSuperclass
public abstract class Rating<T> {

  public static final int RATING_GOOD = 10;
  public static final int RATING_OKAY = 5;
  public static final int RATING_BAD = 0;
  public static final int RATING_NO_SCORE = -1;

  public static String RATING_LABEL_GOOD = "good";
  public static String RATING_LABEL_OKAY = "okay";
  public static String RATING_LABEL_BAD = "bad";
  public static String RATING_LABEL_NO_SCORE = "noscore";
  public static String RATING_LABEL_NONE = "none";

  private static Map<String, Integer> RATING_LABEL_MAP = new HashMap<String, Integer>();

  static {
    RATING_LABEL_MAP.put(RATING_LABEL_GOOD, RATING_GOOD);
    RATING_LABEL_MAP.put(RATING_LABEL_BAD, RATING_BAD);
    RATING_LABEL_MAP.put(RATING_LABEL_OKAY, RATING_OKAY);
    RATING_LABEL_MAP.put(RATING_LABEL_NO_SCORE, RATING_NO_SCORE);
    RATING_LABEL_MAP.put(RATING_LABEL_NONE, null);
  }

  public static Integer getRatingFromLabel(String ratingLabel) {
    if (!RATING_LABEL_MAP.containsKey(ratingLabel)) {
      throw new IllegalArgumentException("Invalid rating: '" + ratingLabel + "'");
    }
    return RATING_LABEL_MAP.get(ratingLabel);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @Column(nullable = false)
  private int rating;
  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean disabled;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public abstract T getItem();

  public abstract void setItem(T item);

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    if (rating != RATING_GOOD &&
        rating != RATING_OKAY &&
        rating != RATING_BAD &&
        rating != RATING_NO_SCORE) {
      throw new IllegalArgumentException("Invalid rating: " + rating);
    }
    this.rating = rating;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  /**
   * True if an administrator has disabled this rating from being included in the rating
   * calculation due to down voting. This is different from setting the rating to "no score"
   * because the owner of the rating will still see the rating as good/bad/okay and will
   * not be aware that the admin has disabled the vote (to avoid the user down voting again).
   *
   * @return true if the rating has been disabled
   */
  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }
}
