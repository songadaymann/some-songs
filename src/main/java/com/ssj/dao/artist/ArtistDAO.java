package com.ssj.dao.artist;

import com.ssj.model.artist.Artist;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.user.User;
import com.ssj.dao.SearchDAO;

import java.util.List;

/**
 * User: sam
 * Date: Mar 9, 2007
 * Time: 9:17:56 AM
 * $Id$
 */
public interface ArtistDAO extends SearchDAO<Artist, ArtistSearch> {

  List getFirstCharactersOfArtistNames();

  boolean isOtherUser(Artist artist, User user);

  List getArtistsByOtherUser(User user);

  Artist findArtistByName(String artistName);

  List<Artist> findArtistsForBandcampSynch();
}
