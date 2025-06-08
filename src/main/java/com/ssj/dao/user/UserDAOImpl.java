package com.ssj.dao.user;

import com.ssj.model.user.*;
import com.ssj.model.artist.Artist;
import com.ssj.model.song.Song;
import com.ssj.model.playlist.Playlist;
import com.ssj.hibernate.HibernateUtil;
import com.ssj.dao.AbstractSpringDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Criterion;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * User: sam
 * Date: Mar 3, 2007
 * Time: 1:10:18 AM
 * $Id$
 */
@Repository
public class UserDAOImpl extends AbstractSpringDAO<User> implements UserDAO {

  @Autowired
  public UserDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public User findByEmail(String email) {
    return (User) new HibernateUtil(getCurrentSession()).find(User.class, Restrictions.eq("email", email));
  }

  public User findByUsername(String username) {
    return (User) new HibernateUtil(getCurrentSession()).find(User.class, Restrictions.eq("username", username));
  }

  private static final String COUNT_USERS_HQL =
      "select count(id) from User";

  public int countUsers() {
    return ((Long) getCurrentSession().createQuery(COUNT_USERS_HQL).iterate().next()).intValue();
  }

  private static final String COUNT_LOGINS_HQL =
    "select count(id) from User where lastLoginDate >= :date";

  public int countLogins(int recentLoginsDays) {
    Date lastLoginDate = new Date();
    lastLoginDate = DateUtils.truncate(lastLoginDate, Calendar.DAY_OF_MONTH);
    lastLoginDate = DateUtils.add(lastLoginDate, Calendar.DAY_OF_YEAR, -recentLoginsDays);
    int recentLogins = 0;
    Long count = (Long) getCurrentSession().createQuery(COUNT_LOGINS_HQL).setParameter("date", lastLoginDate).iterate().next();
    if (count != null) {
      recentLogins = count.intValue();
    }
    return recentLogins;
  }

  public boolean isIgnoredUser(User user, User ignoredUser) {
    Criterion[] criterion = new Criterion[] {
        Restrictions.eq("user.id", user.getId()),
        Restrictions.eq("ignoredUser.id", ignoredUser.getId())
    };

    return (new HibernateUtil(getCurrentSession()).doCount(IgnoredUser.class, criterion) > 0);
  }

  public boolean isPreferredUser(User user, User preferredUser) {
    Criterion[] criterion = new Criterion[] {
        Restrictions.eq("user.id", user.getId()),
        Restrictions.eq("preferredUser.id", preferredUser.getId())
    };

    return (new HibernateUtil(getCurrentSession()).doCount(PreferredUser.class, criterion) > 0);
  }

  public boolean isFavoriteArtist(User user, Artist artist) {
    Criterion[] criterion = new Criterion[] {
        Restrictions.eq("user.id", user.getId()),
        Restrictions.eq("artist.id", artist.getId())
    };

    return (new HibernateUtil(getCurrentSession()).doCount(FavoriteArtist.class, criterion) > 0);
  }

  public boolean isFavoriteSong(User user, Song song) {
    Criterion[] criterion = new Criterion[] {
        Restrictions.eq("user.id", user.getId()),
        Restrictions.eq("song.id", song.getId())
    };

    return (new HibernateUtil(getCurrentSession()).doCount(FavoriteSong.class, criterion) > 0);
  }

  private static final String FIND_ADMINS_HQL =
    "from User where admin = true";

  public List findAdministrators() {
    return getCurrentSession().createQuery(FIND_ADMINS_HQL).list();
  }

  private static final String FIND_USERS_BY_NAME_HQL =
    "from User where lower(displayName) like :name";

  public List findUsersByName(String name) {
    return getCurrentSession().createQuery(FIND_USERS_BY_NAME_HQL).setParameter("name", "%"+name.toLowerCase()+"%").list();
  }

  public boolean isFavoritePlaylist(User user, Playlist playlist) {
    Criterion[] criterion = new Criterion[] {
        Restrictions.eq("user.id", user.getId()),
        Restrictions.eq("playlist.id", playlist.getId())
    };

    return (new HibernateUtil(getCurrentSession()).doCount(FavoritePlaylist.class, criterion) > 0);
  }

  private static final String UPDATE_LAST_LOGIN_DATE_HQL =
    "update User set lastLoginDate = current_timestamp where username = :username";

  public void updateLastLoginDateByUsername(String username) {
    getCurrentSession().createQuery(UPDATE_LAST_LOGIN_DATE_HQL).setParameter("username", username).executeUpdate();
  }

  private static final String TOP_RATERS_HQL =
    "select u, (select count(s) from SongRating s where s.user = u and s.song.showSong = true) as ratings from User u order by ratings desc";

  public List getTopRaters(int numRaters) {
    return getCurrentSession().createQuery(TOP_RATERS_HQL).setFetchSize(numRaters).setMaxResults(numRaters).list();
  }

  private static final String TOP_COMMENTERS_HQL =
    "select u, (select count(s) from SongComment s where s.user = u and s.song.showSong = true) as numComments from User u order by numComments desc";

  public List getTopCommenters(int numCommenters) {
    return getCurrentSession().createQuery(TOP_COMMENTERS_HQL).setFetchSize(numCommenters).setMaxResults(numCommenters).list();
  }

/*
  private static final int USER_ID_ADMIN = 1;
  private static final int USER_ID_SAMDOUGLASS = 2;
  private static final int USER_ID_BLUE = 3;
  private static final int USER_ID_JB = 4;
  private static final int USER_ID_FRANKIE = 5;
  private static final int USER_ID_KEN = 6;
  private static final int USER_ID_YOOK = 7;

  private static final User USER_ADMIN = new User();
  private static final User USER_SAMDOUGLASS = new User();
  private static final User USER_BLUE = new User();
  private static final User USER_JB = new User();
  private static final User USER_FRANKIE = new User();
  private static final User USER_KEN = new User();
  private static final User USER_YOOK = new User();

  static {
    USER_ADMIN.setId(USER_ID_ADMIN);
    USER_ADMIN.setUsername("admin");
    USER_ADMIN.setPassword("test");
    USER_ADMIN.setAdmin(true);
    USER_ADMIN.setLocation("Berkeley, CA");
    USER_ADMIN.setEmail("sam@bozos.com");
    USER_ADMIN.setShowEmailInUserInfo(false);
    USER_ADMIN.setWebsiteName("MySomeSongs");
    USER_ADMIN.setWebsiteURL("http://127.0.0.1:8080/ssj/");

    USER_SAMDOUGLASS.setId(USER_ID_SAMDOUGLASS);
    USER_SAMDOUGLASS.setUsername("Lunkhead");
    USER_SAMDOUGLASS.setPassword("songstink");
    USER_SAMDOUGLASS.setLocation("Berkeley, CA");
    USER_SAMDOUGLASS.setEmail("sam+somesongs@bozos.com");
    USER_SAMDOUGLASS.setShowEmailInUserInfo(false);
    USER_SAMDOUGLASS.setWebsiteName("Bozos.com");
    USER_SAMDOUGLASS.setWebsiteURL("http://www.bozos.com/");

    USER_BLUE.setId(USER_ID_BLUE);
    USER_BLUE.setUsername("blue");
    USER_BLUE.setPassword("blue");
    USER_BLUE.setLocation("San Jose, CA");
    USER_BLUE.setEmail("blue@songhole.org");
    USER_BLUE.setShowEmailInUserInfo(false);
    USER_BLUE.setWebsiteName("SongHole");
    USER_BLUE.setWebsiteURL("http://www.songhole.org/");

  }
*/
}
