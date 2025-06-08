package com.ssj.model.song.search;

import com.ssj.search.PersistentSearchBase;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * User: sam
 * Date: Mar 1, 2007
 * Time: 4:57:08 PM
 * @version $Id$
 *
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "songSearchId"))
public class SongSearch extends PersistentSearchBase {

  public static final int ORDER_BY_RANDOM = 0;
  public static final int ORDER_BY_CREATE_DATE = 1;
  public static final int ORDER_BY_AVG_RATING = 2;
  public static final int ORDER_BY_NUM_RATINGS = 3;
  public static final int ORDER_BY_LAST_COMMENT_DATE = 4;
  public static final int ORDER_BY_PREFERRED_USERS_RATING = 5;
  public static final int ORDER_BY_LAST_RATED = 6;
  public static final int ORDER_BY_LOST_SONGS = 7;
  public static final int ORDER_BY_ALBUM = 8;

  public static final String NAME_DEFAULT = "Song Search";

  @Min(value = 0, message = "Minimum number of ratings must be greater than zero")
  private Integer numRatingsMin;

  private Integer numRatingsMax;

  @Temporal(TemporalType.DATE)
  private Date createDateMin;

  @Temporal(TemporalType.DATE)
  private Date createDateMax;

  private Integer datePosted;

  private String title;

  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean titleExactMatch = false;

  private String artistName;

  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean artistNameExactMatch = false;

  private String album;

  @Column(columnDefinition = "BOOLEAN")
  private boolean albumExactMatch = false;

  private String artistIds;

  private Integer inPreferredUsersFavorites;

  private Float avgRatingMin;

  private Float avgRatingMax;

  private Integer inUsersFavorites;

  private Integer inUsersFavoriteArtists;

  private Boolean hasComment;

  private Boolean hidden = Boolean.FALSE;

  private Integer notRatedByUser;
  
  private Integer ratedGoodByUser;

  private Integer hasCommentByUser;

  public SongSearch() {
    this(NAME_DEFAULT);
  }

  public SongSearch(String name) {
    super();
    setName(name);
    setOrderBy(ORDER_BY_CREATE_DATE);
  }

  public Integer getNumRatingsMin() {
    return numRatingsMin;
  }

  public void setNumRatingsMin(Integer numRatingsMin) {
    this.numRatingsMin = numRatingsMin;
  }

  public Integer getNumRatingsMax() {
    return numRatingsMax;
  }

  public void setNumRatingsMax(Integer numRatingsMax) {
    this.numRatingsMax = numRatingsMax;
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

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }

  public SongSearch changeArtistName(String artistName) {
    this.artistName = artistName;
    return this;
  }

  public boolean isArtistNameExactMatch() {
    return artistNameExactMatch;
  }

  public void setArtistNameExactMatch(boolean artistNameExactMatch) {
    this.artistNameExactMatch = artistNameExactMatch;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public boolean isAlbumExactMatch() {
    return albumExactMatch;
  }

  public void setAlbumExactMatch(boolean albumExactMatch) {
    this.albumExactMatch = albumExactMatch;
  }

  public String getArtistIds() {
    return artistIds;
  }

  public void setArtistIds(String artistIds) {
    this.artistIds = artistIds;
  }

  public Integer getInPreferredUsersFavorites() {
    return inPreferredUsersFavorites;
  }

  public void setInPreferredUsersFavorites(Integer inPreferredUsersFavorites) {
    this.inPreferredUsersFavorites = inPreferredUsersFavorites;
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

  /**
   * Limits the results to songs that are favorite songs
   * of the user with the given id.
   *
   * @param inUsersFavorites the id of the user who has marked the returned songs as favorites
   */
  public void setInUsersFavorites(Integer inUsersFavorites) {
    this.inUsersFavorites = inUsersFavorites;
  }

  public Integer getInUsersFavorites() {
    return inUsersFavorites;
  }

  /**
   * Sets an upper or lower bound on when the songs
   * found by the search were posted. Positive
   * values indicate that songs should have been posted
   * within that many days of today, with 0-1 both meaning
   * songs posted today. Negative values indicate that
   * songs should have been posted -before- that many days
   * ago, with 0-1 both meaning songs posted before today.
   *
   * @param datePosted the value indicating when the date was posted relative to today
   */
  public void setDatePosted(Integer datePosted) {
    this.datePosted = datePosted;
  }

  public Integer getDatePosted() {
    return datePosted;
  }

  public Integer getInUsersFavoriteArtists() {
    return inUsersFavoriteArtists;
  }

  public void setInUsersFavoriteArtists(Integer inUsersFavoriteArtists) {
    this.inUsersFavoriteArtists = inUsersFavoriteArtists;
  }

  public Integer getNotRatedByUser() {
    return notRatedByUser;
  }

  public void setNotRatedByUser(Integer notRatedByUser) {
    this.notRatedByUser = notRatedByUser;
  }

  public Integer getRatedGoodByUser() {
    return ratedGoodByUser;
  }

  public void setRatedGoodByUser(Integer ratedGoodByUser) {
    this.ratedGoodByUser = ratedGoodByUser;
  }

  public Integer getHasCommentByUser() {
    return hasCommentByUser;
  }

  public void setHasCommentByUser(Integer hasCommentByUser) {
    this.hasCommentByUser = hasCommentByUser;
  }

  public boolean isAdvanced() {
    return (artistName != null || numRatingsMin != null || datePosted != null || inUsersFavorites != null ||
        inUsersFavoriteArtists != null || hasCommentByUser != null || notRatedByUser != null);
  }

  public void setAdvanced() {
    // do nothing
  }
}
