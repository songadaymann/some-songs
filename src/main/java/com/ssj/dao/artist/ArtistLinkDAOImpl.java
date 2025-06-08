package com.ssj.dao.artist;

import com.ssj.model.artist.ArtistLink;
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
public class ArtistLinkDAOImpl extends AbstractSpringDAO<ArtistLink> implements ArtistLinkDAO {
  @Autowired
  public ArtistLinkDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
