package com.ssj.model.song;

import com.ssj.model.artist.Artist;
import com.ssj.model.base.Rateable;
import com.ssj.model.user.FavoriteSong;
import com.ssj.util.HashUtil;
import com.ssj.util.SEOUtil;
import com.ssj.util.UrlUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * User: sam
 * Date: Feb 27, 2007
 * Time: 11:00:59 PM
 * @version $Id$
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Song extends Rateable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "songId")
  private int id;

  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean showSong = true;

  @ManyToOne
  @JoinColumn(name = "artistId", nullable = false)
  private Artist artist;

  @NotEmpty(message = "Title is required")
  @Length(max = 256, message = "Title cannot be longer than 256 characters")
  @Column(nullable = false)
  private String title;

  @Transient
  private String titleForUrl;

  @Length(max = 256, message = "Album name cannot be longer than 256 characters")
  private String album;

  @Column(nullable = true)
  private Long duration;

  @Column(nullable = true)
  private Integer albumTrackNumber;

//  @NotEmpty(message = "URL is required")
  @Length(max = 512, message = "URL cannot be longer than 512 characters")
  @Pattern(regexp = "https?://([a-zA-Z0-9\\-]+\\.)+([a-zA-Z][a-zA-Z0-9\\-]+)(:\\d+)?/[^\\?#]+.*",
           message = "Please enter a valid URL for the song")
  @Column(nullable = false)
  private String url;

  /**
   * Optional URL to the Bandcamp page for this song.
   */
  @Length(max = 2000, message = "Bandcamp URL cannot be longer than 2000 characters")
  @Column(nullable = true)
  private String bandcampUrl;

  /**
   * A hash of the Bandcamp URL. MySQL won't allow a unique constraint on a text/clob column
   * so there can't be a unique constraint on the URL itself. The hash should be short enough and
   * unique enough to work as a proxy for the unique constraint.
   */
  @Column(nullable = true, unique = true, length = 200)
  private String bandcampUrlHash;

  @Column(nullable = true, unique = true)
  private Long bandcampTrackId;

  /**
   * Optional URL to the SoundCloud page for this song.
   */
  @Length(max = 2000, message = "SoundCloud URL cannot be longer than 2000 characters")
  @Column(nullable = true)
  private String soundCloudUrl;

  /**
   * A hash of the SoundCloud URL. MySQL won't allow a unique constraint on a text/clob column
   * so there can't be a unique constraint on the URL itself. The hash should be short enough and
   * unique enough to work as a proxy for the unique constraint.
   */
  @Column(nullable = true, unique = true, length = 200)
  private String soundCloudUrlHash;

  @Column(nullable = true, unique = true)
  private Long soundCloudTrackId;

  @NotEmpty(message = "Info is required")
  @Length(max = 1000, message = "Info cannot be longer than 1000 characters")
  @Column(nullable = false)
  @Lob
  private String info;

  @Length(max = 3000, message = "More Info cannot be longer than 3000 characters")
  @Lob
  private String moreInfo;

  @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<SongComment> comments = new LinkedHashSet<SongComment>();

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastLinkCheckDate;

  @OneToMany(mappedBy = "song", cascade = CascadeType.ALL)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<SongRating> ratings = new LinkedHashSet<SongRating>();

  // here for cascade delete
  @OneToMany(mappedBy = "song", cascade = CascadeType.ALL)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<FavoriteSong> favorites = new LinkedHashSet<FavoriteSong>();

  // here for cascade delete
  @OneToMany(mappedBy = "song", cascade = CascadeType.ALL)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<BrokenLinkReport> brokenLinkReports = new LinkedHashSet<BrokenLinkReport>();

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public Song() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public String getTitle() {
    return title;
  }

  public String getTitleForUrl() {
    if (titleForUrl == null) {
      titleForUrl = SEOUtil.cleanForUrl(title);
    }
    return titleForUrl;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  /**
   * Duration in milliseconds.
   */
  public Long getDuration() {
    return duration;
  }

  /**
   * Set duration in milliseconds.
   */
  public void setDuration(Long durationMillis) {
    this.duration = durationMillis;
  }

  public Integer getAlbumTrackNumber() {
    return albumTrackNumber;
  }

  public void setAlbumTrackNumber(Integer albumTrackNumber) {
    this.albumTrackNumber = albumTrackNumber;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getBandcampUrl() {
    return UrlUtil.trimQueryString(bandcampUrl);
  }

  public void setBandcampUrl(String bandcampUrl) {
    this.bandcampUrl = bandcampUrl;
    if (StringUtils.isBlank(bandcampUrl)) {
      this.bandcampUrl = null;
      setBandcampUrlHash(null);
    } else {
      int indexOfQuestionMark = bandcampUrl.indexOf('?');
      if (indexOfQuestionMark > -1) {
        bandcampUrl = bandcampUrl.substring(0, indexOfQuestionMark);
      }
      setBandcampUrlHash(HashUtil.getSha256Hash(bandcampUrl));
    }
  }

  public String getBandcampUrlHash() {
    return bandcampUrlHash;
  }

  public void setBandcampUrlHash(String bandcampUrlHash) {
    this.bandcampUrlHash = bandcampUrlHash;
  }

  public Long getBandcampTrackId() {
    return bandcampTrackId;
  }

  public boolean isImportedFromBandcamp() {
    return (bandcampTrackId != null);
  }

  public void setBandcampTrackId(Long bandcampTrackId) {
    this.bandcampTrackId = bandcampTrackId;
  }

  public String getSoundCloudUrl() {
    return soundCloudUrl;
  }

  public void setSoundCloudUrl(String soundCloudUrl) {
    this.soundCloudUrl = soundCloudUrl;
  }

  public String getSoundCloudUrlHash() {
    return soundCloudUrlHash;
  }

  public void setSoundCloudUrlHash(String soundCloudUrlHash) {
    this.soundCloudUrlHash = soundCloudUrlHash;
  }

  public Long getSoundCloudTrackId() {
    return soundCloudTrackId;
  }

  public void setSoundCloudTrackId(Long soundCloudTrackId) {
    this.soundCloudTrackId = soundCloudTrackId;
  }

  public boolean isImportedFromSoundCloud() {
    return (soundCloudTrackId != null);
  }

  public boolean isSelfHosted() {
    return !isImportedFromBandcamp() && !isImportedFromSoundCloud();
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getMoreInfo() {
    return moreInfo;
  }

  public void setMoreInfo(String moreInfo) {
    this.moreInfo = moreInfo;
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

  public Set<SongComment> getComments() {
    return comments;
  }

  public void setComments(Set<SongComment> comments) {
    this.comments = comments;
  }

  public boolean isShowSong() {
    return showSong;
  }

  public void setShowSong(boolean showSong) {
    this.showSong = showSong;
  }

  public Set<SongRating> getRatings() {
    return ratings;
  }

  public void setRatings(Set<SongRating> ratings) {
    this.ratings = ratings;
  }

  public Date getLastLinkCheckDate() {
    return lastLinkCheckDate;
  }

  public void setLastLinkCheckDate(Date lastLinkCheckDate) {
    this.lastLinkCheckDate = lastLinkCheckDate;
  }

  public Set<FavoriteSong> getFavorites() {
    return favorites;
  }

  public void setFavorites(Set<FavoriteSong> favorites) {
    this.favorites = favorites;
  }

  public Set<BrokenLinkReport> getBrokenLinkReports() {
    return brokenLinkReports;
  }

  public void setBrokenLinkReports(Set<BrokenLinkReport> brokenLinkReports) {
    this.brokenLinkReports = brokenLinkReports;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Song)) return false;

    Song song = (Song) o;

    if (id != song.id) return false;
    if (showSong != song.showSong) return false;
    if (artist != null ? !artist.equals(song.artist) : song.artist != null) return false;
    if (createDate != null ? !createDate.equals(song.createDate) : song.createDate != null) return false;
    if (info != null ? !info.equals(song.info) : song.info != null) return false;
    if (moreInfo != null ? !moreInfo.equals(song.moreInfo) : song.moreInfo != null) return false;
    if (title != null ? !title.equals(song.title) : song.title != null) return false;
    if (url != null ? !url.equals(song.url) : song.url != null) return false;
    if (bandcampUrl != null ? !bandcampUrl.equals(song.bandcampUrl) : song.bandcampUrl != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = id;
    result = 31 * result + (showSong ? 1 : 0);
    result = 31 * result + (artist != null ? artist.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    result = 31 * result + (bandcampUrl != null ? bandcampUrl.hashCode() : 0);
    result = 31 * result + (info != null ? info.hashCode() : 0);
    result = 31 * result + (moreInfo != null ? moreInfo.hashCode() : 0);
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    return result;
  }
}
