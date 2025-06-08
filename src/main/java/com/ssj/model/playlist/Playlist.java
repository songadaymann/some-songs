package com.ssj.model.playlist;

import com.ssj.model.user.User;
import com.ssj.model.user.FavoritePlaylist;
import com.ssj.model.base.Rateable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
public class Playlist extends Rateable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "playlistId")
  private int id;

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private User user;

//  @NotEmpty(message = "Please enter a title")
  @Length(min = 2, max = 256, message = "Title must be 2 to 256 characters long")
  @Column(nullable = false)
  private String title;

  @Length(max = 2000, message = "Description can not contain more than 2000 characters")
  @Lob
  private String description;

  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean publiclyAvailable = true;

  @Column(nullable = false)
  private int numItems; // updated by a trigger

  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("ordinal asc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<PlaylistItem> items = new HashSet<PlaylistItem>();

  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<PlaylistComment> comments = new LinkedHashSet<PlaylistComment>();

  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<PlaylistRating> ratings = new LinkedHashSet<PlaylistRating>();

  // this is really just here for now to handle deleting these when deleting a playlist
  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<FavoritePlaylist> favorites = new LinkedHashSet<FavoritePlaylist>();

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public Playlist() {
  }

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isPubliclyAvailable() {
    return publiclyAvailable;
  }

  public void setPubliclyAvailable(boolean publiclyAvailable) {
    this.publiclyAvailable = publiclyAvailable;
  }

  public Set<PlaylistItem> getItems() {
    return items;
  }

  public void setItems(Set<PlaylistItem> items) {
    this.items = items;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Playlist playlist = (Playlist) o;

    return id == playlist.id;

  }

  public int hashCode() {
    int result;
    result = id;
    result = 31 * result + (user != null ? user.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (publiclyAvailable ? 1 : 0);
//    result = 31 * result + (items != null ? items.hashCode() : 0);
    return result;
  }

  public Set<PlaylistRating> getRatings() {
    return ratings;
  }

  public void setRatings(Set<PlaylistRating> ratings) {
    this.ratings = ratings;
  }

  public int getNumItems() {
    return numItems;
  }

  public void setNumItems(int numItems) {
    this.numItems = numItems;
  }

  public Set<PlaylistComment> getComments() {
    return comments;
  }

  public void setComments(Set<PlaylistComment> comments) {
    this.comments = comments;
  }

  public Set<FavoritePlaylist> getFavorites() {
    return favorites;
  }

  public void setFavorites(Set<FavoritePlaylist> favorites) {
    this.favorites = favorites;
  }
}
