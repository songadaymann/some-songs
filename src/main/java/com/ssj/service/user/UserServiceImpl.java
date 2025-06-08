package com.ssj.service.user;

import com.ssj.dao.user.FavoriteSongsDAO;
import com.ssj.dao.user.IgnoredUserDAO;
import com.ssj.dao.user.PreferredUserDAO;
import com.ssj.dao.user.UserDAO;
import com.ssj.model.user.*;
import com.ssj.model.artist.Artist;
import com.ssj.model.song.Song;
import com.ssj.model.playlist.Playlist;
import com.ssj.service.artist.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.Resource;
import java.util.*;

/**
 * @version $Id$
 */
@Service("userService") // need this for ref from spring security config xml to work (not sure why though?)
@Transactional(readOnly = true)
public class UserServiceImpl
implements   UserService, ApplicationListener<UserEvent>, ApplicationEventPublisherAware {

  protected final Log logger = LogFactory.getLog(getClass());

  private UserDAO userDAO;

  private ArtistService artistService;

  private MailSender mailSender;

  private MessageSource messageSource;

  private String siteUrl;
  private SimpleMailMessage registrationEmail;
  private SimpleMailMessage newPasswordEmail;
  private SimpleMailMessage loginReminderEmail;
  private SimpleMailMessage newAdminMessageEmail;
  private SimpleMailMessage brokenLinkEmail;

  private VelocityEngine velocityEngine;

  private boolean allowAllUsersToPostSongs = true;
  private int songPostLimitHours;

  private PreferredUserDAO preferredUserDAO;
  private IgnoredUserDAO ignoredUserDAO;
  private FavoriteSongsDAO favoriteSongDAO;
  private ApplicationEventPublisher applicationEventPublisher;

  public UserServiceImpl() {

  }

  @Transactional(readOnly = false)
  public void updateMyInfo(User user) {
    if (user == null || user.getId() == 0) {
      throw new UserException("User does not exist");
    }
    User userByUsername = findByLogin(user.getUsername());
    if (userByUsername != null && userByUsername.getId() != user.getId()) {
      throw new UserException("Username '" + user.getUsername() + "' already in use by another user.");
    }
    User userByEmail = findByEmail(user.getEmail());
    if (userByEmail != null && userByEmail.getId() != user.getId()) {
      throw new UserException("E-mail '" + user.getEmail() + "' already in use by another user.");
    }
    try {
      userDAO.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new UserException(e);
    }
  }

  @Transactional(readOnly = false)
  public void save(User user) {
    userDAO.save(user);
  }

  public User findByEmail(String email) {
    return userDAO.findByEmail(email);
  }

  public User findByLogin(String login) {
    return userDAO.findByUsername(login);
  }

  public void checkUserInfoAvailability(User user) {
    if (user.getId() != 0) {
      throw new UserException("User "+user.getDisplayName()+" already registered");
    }

    // TODO check if username is one of the spring security principal constants like "roleAnonymous"

    if (findByLogin(user.getUsername()) != null) {
      throw new UserException("Username '" + user.getUsername() + "' already registered.");
    }

    if (StringUtils.isNotBlank(user.getEmail()) && findByEmail(user.getEmail()) != null) {
      throw new UserException("E-mail '" + user.getEmail() + "' already registered.");
    }
  }

  @Transactional(readOnly = false)
  public void registerUser(User user) {

    checkUserInfoAvailability(user);

    String password = generatePassword();
    user.setPassword(password);

    if (userDAO.countUsers() == 0) {
      // make first registered user an admin
      user.setAdmin(true);
    }
    // store user and send e-mail with password

    userDAO.save(user);

    if (StringUtils.isNotBlank(user.getEmail())) {
      sendWelcomeMessage(user);
    }
  }

  private void sendWelcomeMessage(final User user) {
    SimpleMailMessage mail = new SimpleMailMessage(registrationEmail);
    mail.setTo(user.getEmail());

    Map<String, Object> model = new HashMap<String, Object>();
    model.put("username", user.getUsername());
    model.put("password", user.getPassword());
    model.put("siteurl", siteUrl);

    String result = VelocityEngineUtils.mergeTemplateIntoString(
      velocityEngine, "registrationMessage.vm", "UTF-8", model)
    ;

    mail.setText(result);

    setSiteName(mail);

    mailSender.send(mail);
  }

  private static final String SITE_NAME_PLACEHOLDER = "site.name";

  public void setSiteName(SimpleMailMessage message) {
    String siteName = messageSource.getMessage("site.name", null, Locale.getDefault());
    message.setSubject(message.getSubject().replaceAll(SITE_NAME_PLACEHOLDER, siteName));
    message.setText(message.getText().replaceAll(SITE_NAME_PLACEHOLDER, siteName));
  }

  @Transactional(readOnly = false)
  public void sendNewPassword(String login) {
    if (StringUtils.isEmpty(login)) {
      throw new UserException("Please enter your login.");
    }

    User user = findByLogin(login);

    if (user == null) {
      throw new UserException("Could not find user with login '" + login + "'.");
    }

    if (StringUtils.isBlank(user.getEmail())) {
      throw new UserException("Your account does not have an email address. You probably signed up using your " +
          "Twitter or Facebook. Try signing in with your Twitter of Facebook account.");
    }

    String newPassword = generatePassword();
    user.setPassword(newPassword);

    userDAO.save(user);

    SimpleMailMessage mail = new SimpleMailMessage(newPasswordEmail);
    mail.setTo(user.getEmail());

    Map<String, Object> model = new HashMap<String, Object>();
    model.put("username", user.getUsername());
    model.put("password", user.getPassword());
    model.put("siteurl", siteUrl);

    String result = VelocityEngineUtils.mergeTemplateIntoString(
        velocityEngine, "newPasswordMessage.vm", "UTF-8", model
    );

    mail.setText(result);

    setSiteName(mail);

    mailSender.send(mail);
  }

  public void sendLoginReminder(String email) {
    if (StringUtils.isEmpty(email)) {
      throw new UserException("Please enter the e-mail address for your account.");
    }

    User user = findByEmail(email);

    if (user == null) {
      throw new UserException("Could not find user with e-mail '" + email + "'.");
    }

    SimpleMailMessage mail = new SimpleMailMessage(loginReminderEmail);
    mail.setTo(email);

    Map<String, Object> model = new HashMap<String, Object>();
    model.put("username", user.getUsername());
    model.put("siteurl", siteUrl);

    String result = VelocityEngineUtils.mergeTemplateIntoString(
        velocityEngine, "loginReminderMessage.vm", "UTF-8", model
    );

    mail.setText(result);

    setSiteName(mail);

    mailSender.send(mail);
  }

  @Transactional(readOnly = false)
  public void addArtist(User user, Artist artist) {
    if (artistService.findArtistByName(artist.getName()) != null) {
      throw new UserException("There is already an artist with that name, please pick a different name");
    }
    // reattach security user to hibernate session
//    user = getUser(user.getId());
    Set<Artist> artists = user.getArtists();
    if (artists == null) {
      artists = new HashSet<Artist>();
      user.setArtists(artists);
    }
    user.getArtists().add(artist);
    artist.setUser(user);
    userDAO.save(user);
  }

  public User getUser(int id) {
    return userDAO.get(id);
  }

  @Transactional(readOnly = false)
  public void updateLastLoginTime(String username) {
    userDAO.updateLastLoginDateByUsername(username);
  }

  private static final int RECENT_LOGINS_DAYS = 28;

  public int countRecentLogins() {
    return userDAO.countLogins(RECENT_LOGINS_DAYS);
  }

  @Transactional(readOnly = false)
  public void toggleFavoriteSong(User user, Song song) {
    List<FavoriteSong> favoriteSongs = favoriteSongDAO.findByUser(user);
    FavoriteSong favorite = null;
    for (FavoriteSong favoriteTemp : favoriteSongs) {
      if (favoriteTemp.getSong().getId() == song.getId()) {
        favorite = favoriteTemp;
        break;
      }
    }
    FavoriteEvent event = new FavoriteEvent(this, user);
    event.setObjectType("song");
    event.setObjectId(song.getId());
    if (favorite != null) {
      favoriteSongDAO.delete(favorite);
      event.setActionValue(FavoriteEvent.OFF_ACTION_VALUE);
    } else {
      favorite = new FavoriteSong();
      favorite.setUser(user);
      favorite.setSong(song);
      favoriteSongDAO.save(favorite);
      event.setActionValue(FavoriteEvent.ON_ACTION_VALUE);
    }
    applicationEventPublisher.publishEvent(event);
  }

  @Transactional(readOnly = false)
  public void toggleFavoriteArtist(User user, Artist artist) {
    // reattach security user to hibernate session
//    user = getUser(user.getId());
    Set<FavoriteArtist> favoriteArtists = user.getFavoriteArtists();
    FavoriteArtist favorite = null;
    for (FavoriteArtist favoriteTemp : favoriteArtists) {
      if (favoriteTemp.getArtist().getId() == artist.getId()) {
        favorite = favoriteTemp;
        break;
      }
    }
    FavoriteEvent event = new FavoriteEvent(this, user);
    event.setObjectType("artist");
    event.setObjectId(artist.getId());
    if (favorite != null) {
      favoriteArtists.remove(favorite);
      event.setActionValue(FavoriteEvent.OFF_ACTION_VALUE);
    } else {
      favorite = new FavoriteArtist();
      favorite.setUser(user);
      favorite.setArtist(artist);
      favoriteArtists.add(favorite);
      event.setActionValue(FavoriteEvent.ON_ACTION_VALUE);
    }
    save(user);
    applicationEventPublisher.publishEvent(event);
  }

  @Transactional(readOnly = false)
  public void toggleIgnoredUser(User user, User ignoredUser) {
    // reattach security user to hibernate session
//    user = getUser(user.getId());
    Set<IgnoredUser> ignoredUsers = user.getIgnoredUsers();
    IgnoredUser ignored = null;
    for (IgnoredUser ignoredTemp : ignoredUsers) {
      if (ignoredTemp.getIgnoredUser().getId() == ignoredUser.getId()) {
        ignored = ignoredTemp;
        break;
      }
    }
    if (ignored != null) {
      ignoredUsers.remove(ignored);
    } else {
      ignored = new IgnoredUser();
      ignored.setUser(user);
      ignored.setIgnoredUser(ignoredUser);
      ignoredUsers.add(ignored);
    }
    save(user);
  }

  @Transactional(readOnly = false)
  public void togglePreferredUser(User user, User preferredUser) {
    // reattach security user to hibernate session
//    user = getUser(user.getId());
    Set<PreferredUser> preferredUsers = user.getPreferredUsers();
    PreferredUser preferred = null;
    for (PreferredUser preferredTemp : preferredUsers) {
      if (preferredTemp.getPreferredUser().getId() == preferredUser.getId()) {
        preferred = preferredTemp;
        break;
      }
    }
    if (preferred != null) {
      preferredUsers.remove(preferred);
    } else {
      preferred = new PreferredUser();
      preferred.setUser(user);
      preferred.setPreferredUser(preferredUser);
      preferredUsers.add(preferred);
    }
    save(user);
  }

  public boolean isFavoriteArtist(User user, Artist artist) {
    return userDAO.isFavoriteArtist(user, artist);
  }

  public boolean isFavoriteSong(User user, Song song) {
    return userDAO.isFavoriteSong(user, song);
  }

  public boolean isIgnoredUser(User user, User ignoredUser) {
    return userDAO.isIgnoredUser(user, ignoredUser);
  }

  public boolean isPreferredUser(User user, User preferredUser) {
    return userDAO.isPreferredUser(user, preferredUser);
  }

  /**
   * Checks if the given user can post a song at the moment, and
   * throws an exception if not. Users can only post one song a
   * day. There may be other reasons for preventing users from
   * posting songs in the future that could be added here.
   *
   * @param user the user
   */
  public void isAbleToPostSong(User user) {
    if (!allowAllUsersToPostSongs && !user.isCanPostSongs()) {
      throw new UserException("You do not have permission to post songs.");
    }
    Date lastSongPostDate = user.getLastSongPostDate();
    if (lastSongPostDate != null) {
      long millisSinceLastSongPost = (new Date().getTime() - lastSongPostDate.getTime());
      // get user time limit, or system time limit if none is set for the user
      int userSongPostLimitHours = (user.getSongPostLimitHours() != null ?
                                    user.getSongPostLimitHours() : songPostLimitHours);
      if (userSongPostLimitHours > 0) {
        // user may only post a song every X hours
        if (millisSinceLastSongPost < (userSongPostLimitHours * DateUtils.MILLIS_PER_HOUR)) {
          Date nextPostDate = DateUtils.addHours(lastSongPostDate, userSongPostLimitHours);
          throw new UserException(
            String.format(
              "You may only post one song every %s hour(s), please wait till %s. " +
              "If you would like to be able to post more frequently, please contact the admin.",
                userSongPostLimitHours,
              DateFormatUtils.format(nextPostDate, "h:mma, MMMM d")
            )
          );
        }
      }
    }
  }

  public boolean isAbleToAddArtist(User user) {
    return (allowAllUsersToPostSongs || user.isCanPostSongs());
  }

  public void sendNewAdminMessageEmail() {
    List admins = userDAO.findAdministrators();

    for (Object adminObj : admins) {
      User admin = (User) adminObj;

      SimpleMailMessage mail = new SimpleMailMessage(newAdminMessageEmail);

      mail.setTo(admin.getEmail());

      Map<String, Object> model = new HashMap<String, Object>();
//      model.put("username", user.getUsername());
      model.put("siteurl", siteUrl);

      String result = VelocityEngineUtils.mergeTemplateIntoString(
          velocityEngine, "newAdminMessage.vm", "UTF-8", model
      );

      mail.setText(result);

      setSiteName(mail);

      mailSender.send(mail);
    }
  }

  public List findUsersByName(String name) {
    return userDAO.findUsersByName(name);
  }

  @Transactional(readOnly = false)
  public void toggleFavoritePlaylist(User user, Playlist playlist) {
    // reattach security user to hibernate session
//    user = getUser(user.getId());
    Set<FavoritePlaylist> favoritePlaylists = user.getFavoritePlaylists();
    FavoritePlaylist favorite = null;
    for (FavoritePlaylist favoriteTemp : favoritePlaylists) {
      if (favoriteTemp.getPlaylist().getId() == playlist.getId()) {
        favorite = favoriteTemp;
        break;
      }
    }
    FavoriteEvent event = new FavoriteEvent(this, user);
    event.setObjectType("playlist");
    event.setObjectId(playlist.getId());
    if (favorite != null) {
      favoritePlaylists.remove(favorite);
      event.setActionValue(FavoriteEvent.OFF_ACTION_VALUE);
    } else {
      favorite = new FavoritePlaylist();
      favorite.setUser(user);
      favorite.setPlaylist(playlist);
      favoritePlaylists.add(favorite);
      event.setActionValue(FavoriteEvent.ON_ACTION_VALUE);
    }
    save(user);
    applicationEventPublisher.publishEvent(event);
  }

  public boolean isFavoritePlaylist(User user, Playlist playlist) {
    return userDAO.isFavoritePlaylist(user, playlist);
  }

  public void sendBrokenLinkEmail(Song song) {
    SimpleMailMessage mail = new SimpleMailMessage(brokenLinkEmail);
    Artist artist = song.getArtist();
    User user = artist.getUser();
    if (user.getEmail() == null) {
      // no email, signed up via social sign in
      return;
    }
    mail.setTo(user.getEmail());

    List admins = userDAO.findAdministrators();
    String[] adminEmails = new String[admins.size()];
    for (int i = 0; i < admins.size(); i++) {
      User admin = (User) admins.get(i);
      adminEmails[i] = admin.getEmail();
    }
    mail.setBcc(adminEmails);

    Map<String, Object> model = new HashMap<String, Object>();
    model.put("displayName", user.getDisplayName());
    model.put("songTitle", song.getTitle());
    model.put("artistName", artist.getName());
    model.put("siteurl", siteUrl);
    model.put("songId", Integer.toString(song.getId()));
    model.put("songUrl", song.getUrl());

    String result = VelocityEngineUtils.mergeTemplateIntoString(
        velocityEngine, "brokenLinkMessage.vm", "UTF-8", model
    );

    mail.setText(result);

    setSiteName(mail);

    mailSender.send(mail);
  }

  public List<PreferredUser> getPreferredUsers(User user) {
    return preferredUserDAO.findByUser(user);
  }

  public List<IgnoredUser> getIgnoredUsers(User user) {
    return ignoredUserDAO.findByUser(user);
  }

  private static final int NUM_TOP_USERS = 10;

  @Override
  public List getTopRaters() {
    return userDAO.getTopRaters(NUM_TOP_USERS);
  }

  @Override
  public List getTopCommenters() {
    return userDAO.getTopCommenters(NUM_TOP_USERS);
  }

  private static final String RANDOM_PASSWORD_CHARS =
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_!$*";

  private static final int RANDOM_PASSWORD_LENGTH = 12;

  private String generatePassword() {
    StringBuffer password = new StringBuffer();
    for (int i = 0; i < RANDOM_PASSWORD_LENGTH; i++) {
      int charIndex = (int) (Math.random() * RANDOM_PASSWORD_CHARS.length());
      char randomChar = RANDOM_PASSWORD_CHARS.charAt(charIndex);
      password.append(randomChar);
    }
    return password.toString();
  }

  @Autowired
  public void setUserDAO(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Autowired
  public void setPreferredUserDAO(PreferredUserDAO preferredUserDAO) {
    this.preferredUserDAO = preferredUserDAO;
  }

  @Autowired
  public void setIgnoredUserDAO(IgnoredUserDAO ignoredUserDAO) {
    this.ignoredUserDAO = ignoredUserDAO;
  }

  @Resource(name = "mailSender")
  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Autowired
  public void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  @Resource(name = "registrationMessage")
  public void setRegistrationEmail(SimpleMailMessage registrationEmail) {
    this.registrationEmail = registrationEmail;
  }

  @Resource(name = "newPasswordMessage")
  public void setNewPasswordEmail(SimpleMailMessage newPasswordEmail) {
    this.newPasswordEmail = newPasswordEmail;
  }

  @Resource(name = "loginReminderMessage")
  public void setLoginReminderEmail(SimpleMailMessage loginReminderEmail) {
    this.loginReminderEmail = loginReminderEmail;
  }

  @Resource(name = "newAdminMessage")
  public void setNewAdminMessageEmail(SimpleMailMessage newAdminMessageEmail) {
    this.newAdminMessageEmail = newAdminMessageEmail;
  }

  @Resource(name = "brokenLinkMessage")
  public void setBrokenLinkEmail(SimpleMailMessage brokenLinkEmail) {
    this.brokenLinkEmail = brokenLinkEmail;
  }

  @Value("${site.url:http://somesongs.org}")
  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }

  @Value("${allowAllUsersToPostSongs:true}")
  public void setAllowAllUsersToPostSongs(boolean allowAllUsersToPostSongs) {
    this.allowAllUsersToPostSongs = allowAllUsersToPostSongs;
  }

  @Value("${songPostLimitHours:2}")
  public void setSongPostLimitHours(int songPostLimitHours) {
    this.songPostLimitHours = songPostLimitHours;
  }

  @Autowired
  public void setFavoriteSongDAO(FavoriteSongsDAO favoriteSongDAO) {
    this.favoriteSongDAO = favoriteSongDAO;
  }

  @Override
  public void onApplicationEvent(UserEvent event) {
    if (UserEvent.ACTION_LOGIN.equals(event.getAction())) {
      User user = event.getUser();
      user.setLastLoginDate(new Date(event.getTimestamp()));
      save(user);
    }
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }
}
