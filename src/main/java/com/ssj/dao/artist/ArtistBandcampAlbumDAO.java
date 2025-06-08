package com.ssj.dao.artist;

import com.ssj.dao.DAO;
import com.ssj.model.artist.Artist;
import com.ssj.model.artist.ArtistBandcampAlbum;

/**
 * @author sam
 * @version $Id$
 */
public interface ArtistBandcampAlbumDAO extends DAO<ArtistBandcampAlbum> {
  int deleteAllForArtist(Artist artist);
}
