package com.ssj.dao.artist;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.artist.Artist;
import com.ssj.model.artist.ArtistBandcampAlbum;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author sam
 * @version $Id$
 */
@Repository
public class ArtistBandcampAlbumDAOImpl extends AbstractSpringDAO<ArtistBandcampAlbum> implements ArtistBandcampAlbumDAO {
  @Autowired
  public ArtistBandcampAlbumDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  private static final String DELETE_ALL_FOR_ARTIST_HQL =
      "delete from ArtistBandcampAlbum where artist = :artist";

  public int deleteAllForArtist(Artist artist) {
    return getCurrentSession().createQuery(DELETE_ALL_FOR_ARTIST_HQL).setParameter("artist", artist).executeUpdate();
  }
}
