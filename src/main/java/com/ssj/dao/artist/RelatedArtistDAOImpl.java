package com.ssj.dao.artist;

import com.ssj.model.artist.RelatedArtist;
import com.ssj.dao.AbstractSpringDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class RelatedArtistDAOImpl extends AbstractSpringDAO<RelatedArtist> implements RelatedArtistDAO {
  @Autowired
  public RelatedArtistDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
