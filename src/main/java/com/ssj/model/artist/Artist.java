package com.ssj.model.artist;

import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.util.SEOUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Artist {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "artistId")
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="userId")
  private User user; // many-to-one? need this side of relationship?

  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
  @OrderBy("createDate desc")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ArtistOtherUser> otherUsers; // just use this many-to-many? need this side of relationship?

  @NotEmpty(message = "Artist name may not be empty")
  @Length(max = 128)
  @Column(unique = true, nullable = false)
  private String name;

  @Length(max = 1024, message = "Artist info cannot be longer than 1024 characters")
  @Lob
  private String info;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean synchFromBandcamp;

  @Length(max = 2000, message = "Bandcamp URL cannot be longer than 2000 characters")
  private String bandcampUrl;

  private Long bandcampBandId;

//  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
  @OrderBy("createDate desc")
  private Set<ArtistLink> links;

  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createDate desc")
  private Set<RelatedArtist> relatedArtists;

  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
//  @OrderBy("createDate desc")
  private Set<Song> songs;

  @OneToMany(mappedBy = "artist")
  private List<ArtistBandcampAlbum> bandcampAlbums;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  @Transient
  private String nameForUrl;

  public Artist() {

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

  public Set<ArtistOtherUser> getOtherUsers() {
    return otherUsers;
  }

  public void setOtherUsers(Set<ArtistOtherUser> otherUsers) {
    this.otherUsers = otherUsers;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public boolean isSynchFromBandcamp() {
    return synchFromBandcamp;
  }

  public void setSynchFromBandcamp(boolean synchFromBandcamp) {
    this.synchFromBandcamp = synchFromBandcamp;
  }

  public String getBandcampUrl() {
    return bandcampUrl;
  }

  public void setBandcampUrl(String bandcampUrl) {
    this.bandcampUrl = bandcampUrl;
  }

  public Long getBandcampBandId() {
    return bandcampBandId;
  }

  public void setBandcampBandId(Long bandcampBandId) {
    this.bandcampBandId = bandcampBandId;
  }

  public Set<ArtistLink> getLinks() {
    return links;
  }

  public void setLinks(Set<ArtistLink> links) {
    this.links = links;
  }

  public Set<RelatedArtist> getRelatedArtists() {
    return relatedArtists;
  }

  public void setRelatedArtists(Set<RelatedArtist> relatedArtists) {
    this.relatedArtists = relatedArtists;
  }

  public Set<Song> getSongs() {
    return songs;
  }

  public void setSongs(Set<Song> songs) {
    this.songs = songs;
  }

  public List<ArtistBandcampAlbum> getBandcampAlbums() {
    return bandcampAlbums;
  }

  public void setBandcampAlbums(List<ArtistBandcampAlbum> bandcampAlbums) {
    this.bandcampAlbums = bandcampAlbums;
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

  public String getNameForUrl() {
    if (nameForUrl == null) {
      nameForUrl = SEOUtil.cleanForUrl(name);
    }
    return nameForUrl;
  }
}
