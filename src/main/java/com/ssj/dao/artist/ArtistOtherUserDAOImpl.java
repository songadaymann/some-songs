package com.ssj.dao.artist;

import com.ssj.model.artist.ArtistOtherUser;
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
public class ArtistOtherUserDAOImpl extends AbstractSpringDAO<ArtistOtherUser> implements ArtistOtherUserDAO {
  @Autowired
  public ArtistOtherUserDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
