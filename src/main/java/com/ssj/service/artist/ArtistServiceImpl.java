package com.ssj.service.artist;

import com.ssj.dao.artist.*;
import com.ssj.model.artist.*;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: sam
 * Date: Mar 9, 2007
 * Time: 9:17:10 AM
 * $Id$
 */
@Service
@Transactional(readOnly = true)
public class ArtistServiceImpl implements ArtistService {

  private ArtistDAO artistDAO;
  private ArtistLinkDAO artistLinkDAO;
  private ArtistOtherUserDAO artistOtherUserDAO;
  private RelatedArtistDAO relatedArtistDAO;
  private ArtistBandcampAlbumDAO artistBandcampAlbumDAO;

  public ArtistServiceImpl() {

  }

  @Transactional(readOnly = false)
  public void save(Artist artist) {
    artistDAO.save(artist);
  }

  public Artist getArtist(int artistId) {
    return artistDAO.get(artistId);
  }

  public ArtistLink getArtistLink(int artistLinkId) {
    return artistLinkDAO.get(artistLinkId);
  }

  @Transactional(readOnly = false)
  public void saveLink(ArtistLink artistLink) {
    artistLinkDAO.save(artistLink);
  }

  public List<Artist> findArtists(ArtistSearch search) {
    List<Artist> artists = null;
    search.setTotalResults(artistDAO.doCount(search));
    if (search.getTotalResults() > 0) {
      artists = artistDAO.doSearch(search);
    }
    return artists;
  }

  public int countArtists(ArtistSearch search) {
    return artistDAO.doCount(search);
  }

  public List getFirstCharactersOfArtistNames() {
    return artistDAO.getFirstCharactersOfArtistNames();
  }

  @Transactional(readOnly = false)
  public void deleteArtist(Artist artist) {
    artistDAO.delete(artist);
  }

  @Transactional(readOnly = false)
  public void deleteArtistLink(ArtistLink artistLink) {
    artistLinkDAO.delete(artistLink);
  }

  public boolean canEditArtist(Artist artist, User user) {
    boolean canEdit = (artist.getUser().getId() == user.getId() || user.isAdmin());
    if (!canEdit) {
      canEdit = artistDAO.isOtherUser(artist, user);
    }
    return canEdit;
  }

  @Transactional(readOnly = false)
  public void addOtherUser(Artist artist, User user) {
    Set<ArtistOtherUser> otherUsers = artist.getOtherUsers();
    for (ArtistOtherUser otherUser : otherUsers) {
      if (otherUser.getUser().getId() == user.getId()) {
        throw new ArtistException("Artist is already shared with this user");
      }
    }
    ArtistOtherUser otherUser = new ArtistOtherUser();
    otherUser.setArtist(artist);
    otherUser.setUser(user);
    try {
      artistOtherUserDAO.save(otherUser);
    } catch (ConstraintViolationException e) {
      // catch this just in case
      e.printStackTrace();
      throw new ArtistException("Artist is already shared with this user");
    }
  }

  @Transactional(readOnly = false)
  public void deleteOtherUser(Artist artist, User user) {
    ArtistOtherUser selectedArtistOtherUser = null;
    for (ArtistOtherUser artistOtherUser : artist.getOtherUsers()) {
      if (artistOtherUser.getUser().getId() == user.getId()) {
        selectedArtistOtherUser = artistOtherUser;
        break;
      }
    }
    if (selectedArtistOtherUser != null) {
      artist.getOtherUsers().remove(selectedArtistOtherUser);
      artistOtherUserDAO.delete(selectedArtistOtherUser);
    }
  }

  @Transactional(readOnly = false)
  public void addRelatedArtist(Artist artist, Artist relatedArtist) {
    Set<RelatedArtist> relatedArtists = artist.getRelatedArtists();
    for (RelatedArtist relatedArtistObj : relatedArtists) {
      if (relatedArtistObj.getArtist().getId() == relatedArtist.getId()) {
        throw new ArtistException("Artists are already related");
      }
    }
    RelatedArtist relatedArtistObj = new RelatedArtist();
    relatedArtistObj.setArtist(artist);
    relatedArtistObj.setRelatedArtist(relatedArtist);
    try {
      relatedArtistDAO.save(relatedArtistObj);
    } catch (ConstraintViolationException e) {
      // catch this here just in case
      e.printStackTrace();
      throw new ArtistException("Artists are already related");
    }
  }

  public List getArtistsByOtherUser(User user) {
    return artistDAO.getArtistsByOtherUser(user);
  }

  public Artist findArtistByName(String artistName) {
    return artistDAO.findArtistByName(artistName);
  }

  public List<Artist> findArtistsForBandcampSynch() {
    return artistDAO.findArtistsForBandcampSynch();
  }

  @Transactional(readOnly = false)
  public void saveBandcampSycnhAlbums(Artist artist, long[] bandcampAlbumIds) {
    // delete the existing synch albums
    artistBandcampAlbumDAO.deleteAllForArtist(artist);
    // if no ids specified, done
    if (bandcampAlbumIds == null) return;
    // otherwise add the specified ids
    for (long bandcampAlbumId : bandcampAlbumIds) {
      ArtistBandcampAlbum artistBandcampAlbum = new ArtistBandcampAlbum();
      artistBandcampAlbum.setArtist(artist);
      artistBandcampAlbum.setBandcampAlbumId(bandcampAlbumId);
      artistBandcampAlbumDAO.save(artistBandcampAlbum);
    }
  }

  @Transactional(readOnly = false)
  public void deleteRelatedArtist(Artist artist, Artist relatedArtist) {
    Set<RelatedArtist> relatedArtists = new HashSet<RelatedArtist>();
    relatedArtists.addAll(artist.getRelatedArtists());
    for (RelatedArtist relatedArtistObj : relatedArtists) {
      if (relatedArtistObj.getRelatedArtist().getId() == relatedArtist.getId()) {
        relatedArtistObj.setArtist(null);
        artist.getRelatedArtists().remove(relatedArtistObj);
        artistDAO.save(artist);
        break;
      }
    }
  }

  @Autowired
  public void setArtistDAO(ArtistDAO artistDAO) {
    this.artistDAO = artistDAO;
  }

  @Autowired
  public void setArtistLinkDAO(ArtistLinkDAO artistLinkDAO) {
    this.artistLinkDAO = artistLinkDAO;
  }

  @Autowired
  public void setArtistOtherUserDAO(ArtistOtherUserDAO artistOtherUserDAO) {
    this.artistOtherUserDAO = artistOtherUserDAO;
  }

  @Autowired
  public void setRelatedArtistDAO(RelatedArtistDAO relatedArtistDAO) {
    this.relatedArtistDAO = relatedArtistDAO;
  }

  @Autowired
  public void setArtistBandcampAlbumDAO(ArtistBandcampAlbumDAO artistBandcampAlbumDAO) {
    this.artistBandcampAlbumDAO = artistBandcampAlbumDAO;
  }
}
