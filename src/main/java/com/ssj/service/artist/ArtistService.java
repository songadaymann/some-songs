package com.ssj.service.artist;

import com.ssj.model.artist.Artist;
import com.ssj.model.artist.ArtistLink;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.user.User;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface ArtistService {

  void save(Artist artist);

  Artist getArtist(int artistId);

  ArtistLink getArtistLink(int artistLinkId);

  void saveLink(ArtistLink artistLink);

  List<Artist> findArtists(ArtistSearch search);

  int countArtists(ArtistSearch search);

  List getFirstCharactersOfArtistNames();

  void deleteArtist(Artist artist);

  void deleteArtistLink(ArtistLink artistLink);

  boolean canEditArtist(Artist artist, User user);

  void addOtherUser(Artist artist, User user);

  void deleteOtherUser(Artist artist, User user);

  void deleteRelatedArtist(Artist artist, Artist relatedArtist);

  void addRelatedArtist(Artist artist, Artist relatedArtist);

  List getArtistsByOtherUser(User user);

  Artist findArtistByName(String artistName);

  List<Artist> findArtistsForBandcampSynch();

  void saveBandcampSycnhAlbums(Artist artist, long[] bandcampAlbumIds);

}
