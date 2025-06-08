package com.ssj.model.playlist.search;

import com.ssj.search.PersistentSearchBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
public class PlaylistSearch extends PersistentSearchBase {

  public static final int ORDER_BY_RANDOM = 0;
  public static final int ORDER_BY_CREATE_DATE = 1;
  public static final int ORDER_BY_AVG_RATING = 2;
  public static final int ORDER_BY_NUM_RATINGS = 3;
  public static final int ORDER_BY_NUM_SONGS = 4;
  public static final int ORDER_BY_LAST_COMMENT_DATE = 5;
  public static final int ORDER_BY_PREFERRED_USERS_RATING = 6;
  public static final int ORDER_BY_LAST_RATED = 7;
  public static final int ORDER_BY_LOST_SONGS = 8;

  public static final String NAME_DEFAULT = "Playlist Search";

  private Integer numRatingsMin;

  private Integer numRatingsMax;

  private Integer numItemsMin = 1;

  private Integer numItemsMax;

  @Temporal(TemporalType.DATE)
  private Date createDateMin;

  @Temporal(TemporalType.DATE)
  private Date createDateMax;

  private Integer datePosted;

  private String title;

  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean titleExactMatch = false;

  private String userDisplayName;

  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean userDisplayNameExactMatch = false;

  private String songIds;

  private Integer inPreferredUsersFavorites;

  private Float avgRatingMin;

  private Float avgRatingMax;

  private Integer inUsersFavorites;

  private Integer inUsersPreferredUsers;

  private Boolean hasComment;

  private Boolean hidden = Boolean.FALSE;

  private Integer notRatedByUser;

  private Integer hasCommentByUser;

  public PlaylistSearch() {
    this(NAME_DEFAULT);
  }

  public PlaylistSearch(String name) {
    super();
    setName(name);
    setOrderBy(ORDER_BY_CREATE_DATE);
  }

  public Integer getNumRatingsMin() {
    return numRatingsMin;
  }

  public void setNumRatingsMin(Integer numRatingsMin) {
    this.numRatingsMin = (numRatingsMin != null && numRatingsMin > 0 ? numRatingsMin : null);
  }

  public Integer getNumRatingsMax() {
    return numRatingsMax;
  }

  public void setNumRatingsMax(Integer numRatingsMax) {
    this.numRatingsMax = (numRatingsMax != null && numRatingsMax > 0 ? numRatingsMax : null);
  }

  public Date getCreateDateMin() {
    return createDateMin;
  }

  public void setCreateDateMin(Date createDateMin) {
    this.createDateMin = createDateMin;
  }

  public Date getCreateDateMax() {
    return createDateMax;
  }

  public void setCreateDateMax(Date createDateMax) {
    this.createDateMax = createDateMax;
  }

  public Integer getDatePosted() {
    return datePosted;
  }

  public void setDatePosted(Integer datePosted) {
    this.datePosted = datePosted;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isTitleExactMatch() {
    return titleExactMatch;
  }

  public void setTitleExactMatch(boolean titleExactMatch) {
    this.titleExactMatch = titleExactMatch;
  }

  public String getUserDisplayName() {
    return userDisplayName;
  }

  public void setUserDisplayName(String userDisplayName) {
    this.userDisplayName = userDisplayName;
  }

  public boolean isUserDisplayNameExactMatch() {
    return userDisplayNameExactMatch;
  }

  public void setUserDisplayNameExactMatch(boolean userDisplayNameExactMatch) {
    this.userDisplayNameExactMatch = userDisplayNameExactMatch;
  }

  public String getSongIds() {
    return songIds;
  }

  public void setSongIds(String songIds) {
    this.songIds = songIds;
  }

  public Float getAvgRatingMin() {
    return avgRatingMin;
  }

  public void setAvgRatingMin(Float avgRatingMin) {
    this.avgRatingMin = avgRatingMin;
  }

  public Float getAvgRatingMax() {
    return avgRatingMax;
  }

  public void setAvgRatingMax(Float avgRatingMax) {
    this.avgRatingMax = avgRatingMax;
  }

  public Boolean getHasComment() {
    return hasComment;
  }

  public void setHasComment(Boolean hasComment) {
    this.hasComment = hasComment;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public Integer getInUsersFavorites() {
    return inUsersFavorites;
  }

  public void setInUsersFavorites(Integer inUsersFavorites) {
    this.inUsersFavorites = inUsersFavorites;
  }

  public Integer getNumItemsMin() {
    return numItemsMin;
  }

  public void setNumItemsMin(Integer numItemsMin) {
    this.numItemsMin = numItemsMin;
  }

  public Integer getNumItemsMax() {
    return numItemsMax;
  }

  public void setNumItemsMax(Integer numItemsMax) {
    this.numItemsMax = numItemsMax;
  }

  public Integer getInPreferredUsersFavorites() {
    return inPreferredUsersFavorites;
  }

  public void setInPreferredUsersFavorites(Integer inPreferredUsersFavorites) {
    this.inPreferredUsersFavorites = inPreferredUsersFavorites;
  }

  public Integer getInUsersPreferredUsers() {
    return inUsersPreferredUsers;
  }

  public void setInUsersPreferredUsers(Integer inUsersPreferredUsers) {
    this.inUsersPreferredUsers = inUsersPreferredUsers;
  }

  public Integer getNotRatedByUser() {
    return notRatedByUser;
  }

  public void setNotRatedByUser(Integer notRatedByUser) {
    this.notRatedByUser = notRatedByUser;
  }

  public Integer getHasCommentByUser() {
    return hasCommentByUser;
  }

  public void setHasCommentByUser(Integer hasCommentByUser) {
    this.hasCommentByUser = hasCommentByUser;
  }
}
