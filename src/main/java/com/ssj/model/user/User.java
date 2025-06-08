package com.ssj.model.user;

import com.ssj.model.artist.Artist;
import com.ssj.model.song.SongComment;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "userId")
  private int id;

  @Column(nullable = false, unique = true)
  @NotEmpty(message = "Login is required")
  @Length(min = 3, max = 64, message = "Login must be 3 to 64 characters")
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(name = "name", nullable = false)
  @NotEmpty(message = "Display Name is required")
  @Length(min = 3, max = 64, message = "Display Name must be 3 to 64 characters")
  private String displayName;

  @Column(nullable = false, unique = true)
  @Length(max = 128, message = "E-mail cannot be longer than 128 characters")
  @Email(message = "E-mail must be a valid e-mail address")
  private String email;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean showEmailInUserInfo;

  @Length(max = 64, message = "Name of Your Website cannot be longer than 64 characters")
  private String websiteName;

  @Length(max = 128, message = "URL of Your Website cannot be longer than 128 characters")
  private String websiteURL;

  @Column(name = "city")
  @Length(max = 64, message = "Location cannot be longer than 64 characters")
  private String location;

  // consider moving to dependent object
  @Length(max = 64, message = "Good Band name cannot be longer than 64 characters")
  private String goodBand;

  @Length(max = 64, message = "Good Album name cannot be longer than 64 characters")
  private String goodAlbum;

  @Length(max = 64, message = "Good Song name cannot be longer than 64 characters")
  private String goodSong;

  @Length(max = 64, message = "Good Book name cannot be longer than 64 characters")
  private String goodBook;

  @Length(max = 64, message = "Good Movie name cannot be longer than 64 characters")
  private String goodMovie;

  @Length(max = 64, message = "Good Website name cannot be longer than 64 characters")
  private String goodWebsiteName;

  @Length(max = 128, message = "Good Website URL cannot be longer than 128 characters")
  private String goodWebsiteURL;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @OrderBy("name asc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Artist> artists; // = new ArrayList();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<FavoriteSong> favoriteSongs;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<FavoriteArtist> favoriteArtists;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<FavoritePlaylist> favoritePlaylists;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<PreferredUser> preferredUsers;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<IgnoredUser> ignoredUsers;

  @OneToMany(mappedBy = "user")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<SongComment> comments;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastLoginDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastSongPostDate;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean canPostSongs;

  @Column
  private Integer songPostLimitHours;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean canSynchFromBandcamp;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean admin;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean publishToTimeline;

  /**
   * How the user signed up for the site, can be "form",
   * if via the registration form, or "twitter"/"facebook"
   * if via Twitter or Facebook.
   */
  private String signupPath;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public User() {

  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isShowEmailInUserInfo() {
    return showEmailInUserInfo;
  }

  public void setShowEmailInUserInfo(boolean showEmailInUserInfo) {
    this.showEmailInUserInfo = showEmailInUserInfo;
  }

  public String getWebsiteName() {
    return websiteName;
  }

  public void setWebsiteName(String websiteName) {
    this.websiteName = websiteName;
  }

  public String getWebsiteURL() {
    return websiteURL;
  }

  public void setWebsiteURL(String websiteURL) {
    this.websiteURL = websiteURL;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getGoodBand() {
    return goodBand;
  }

  public void setGoodBand(String goodBand) {
    this.goodBand = goodBand;
  }

  public String getGoodAlbum() {
    return goodAlbum;
  }

  public void setGoodAlbum(String goodAlbum) {
    this.goodAlbum = goodAlbum;
  }

  public String getGoodSong() {
    return goodSong;
  }

  public void setGoodSong(String goodSong) {
    this.goodSong = goodSong;
  }

  public String getGoodBook() {
    return goodBook;
  }

  public void setGoodBook(String goodBook) {
    this.goodBook = goodBook;
  }

  public String getGoodMovie() {
    return goodMovie;
  }

  public void setGoodMovie(String goodMovie) {
    this.goodMovie = goodMovie;
  }

  public String getGoodWebsiteName() {
    return goodWebsiteName;
  }

  public void setGoodWebsiteName(String goodWebsiteName) {
    this.goodWebsiteName = goodWebsiteName;
  }

  public String getGoodWebsiteURL() {
    return goodWebsiteURL;
  }

  public void setGoodWebsiteURL(String goodWebsiteURL) {
    this.goodWebsiteURL = goodWebsiteURL;
  }

  public Set<Artist> getArtists() {
    return artists;
  }

  public void setArtists(Set<Artist> artists) {
    this.artists = artists;
  }

  public Set<FavoriteSong> getFavoriteSongs() {
    return favoriteSongs;
  }

  public void setFavoriteSongs(Set<FavoriteSong> favoriteSongs) {
    this.favoriteSongs = favoriteSongs;
  }

  public Set<FavoriteArtist> getFavoriteArtists() {
    return favoriteArtists;
  }

  public void setFavoriteArtists(Set<FavoriteArtist> favoriteArtists) {
    this.favoriteArtists = favoriteArtists;
  }

  public Set<PreferredUser> getPreferredUsers() {
    return preferredUsers;
  }

  public void setPreferredUsers(Set<PreferredUser> preferredUsers) {
    this.preferredUsers = preferredUsers;
  }

  public Set<IgnoredUser> getIgnoredUsers() {
    return ignoredUsers;
  }

  public void setIgnoredUsers(Set<IgnoredUser> ignoredUsers) {
    this.ignoredUsers = ignoredUsers;
  }

  public Set<SongComment> getComments() {
    return comments;
  }

  public void setComments(Set<SongComment> comments) {
    this.comments = comments;
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

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String toString() {
    return "{username : '" + username + "', email : '" + email + "', displayName : '" + displayName + "'}";
  }

  public Date getLastLoginDate() {
    return lastLoginDate;
  }

  public void setLastLoginDate(Date lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  public Date getLastSongPostDate() {
    return lastSongPostDate;
  }

  public void setLastSongPostDate(Date lastSongPostDate) {
    this.lastSongPostDate = lastSongPostDate;
  }

  public Set<FavoritePlaylist> getFavoritePlaylists() {
    return favoritePlaylists;
  }

  public void setFavoritePlaylists(Set<FavoritePlaylist> favoritePlaylists) {
    this.favoritePlaylists = favoritePlaylists;
  }

  public boolean isCanPostSongs() {
    return canPostSongs;
  }

  public void setCanPostSongs(boolean canPostSongs) {
    this.canPostSongs = canPostSongs;
  }

  public Integer getSongPostLimitHours() {
    return songPostLimitHours;
  }

  public void setSongPostLimitHours(Integer postSongLimitHours) {
    this.songPostLimitHours = postSongLimitHours;
  }

  public boolean isCanSynchFromBandcamp() {
    return canSynchFromBandcamp;
  }

  public void setCanSynchFromBandcamp(boolean canSynchFromBandcamp) {
    this.canSynchFromBandcamp = canSynchFromBandcamp;
  }

  public boolean isPublishToTimeline() {
    return publishToTimeline;
  }

  public void setPublishToTimeline(boolean publishToTimeline) {
    this.publishToTimeline = publishToTimeline;
  }

  public String getSignupPath() {
    return signupPath;
  }

  public void setSignupPath(String signupPath) {
    this.signupPath = signupPath;
  }
}
