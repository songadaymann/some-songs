package com.ssj.service.user;

import com.ssj.model.user.IgnoredUser;
import com.ssj.model.user.PreferredUser;
import com.ssj.model.user.User;
import com.ssj.model.artist.Artist;
import com.ssj.model.song.Song;
import com.ssj.model.playlist.Playlist;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface UserService {

  void save(User user);

  User findByEmail(String email);

  User findByLogin(String login);

  void checkUserInfoAvailability(User user);

  void registerUser(User user);

  void sendNewPassword(String login);

  void sendLoginReminder(String email);

  void updateMyInfo(User user);

  void addArtist(User user, Artist artist);

  User getUser(int id);

  void updateLastLoginTime(String username);

  int countRecentLogins();

  void toggleFavoriteSong(User user, Song song);

  void toggleFavoriteArtist(User user, Artist artist);

  void toggleIgnoredUser(User user, User ignoredUser);

  void togglePreferredUser(User user, User preferredUser);

  boolean isFavoriteArtist(User user, Artist artist);

  boolean isFavoriteSong(User user, Song song);

  boolean isIgnoredUser(User user, User ignoredUser);

  boolean isPreferredUser(User user, User preferredUser);

  void isAbleToPostSong(User user);

  boolean isAbleToAddArtist(User user);

  void sendNewAdminMessageEmail();

  List findUsersByName(String name);

  void toggleFavoritePlaylist(User user, Playlist playlist);

  boolean isFavoritePlaylist(User user, Playlist playlist);

  void sendBrokenLinkEmail(Song song);

  List<PreferredUser> getPreferredUsers(User user);

  List<IgnoredUser> getIgnoredUsers(User user);
  
  List getTopRaters();
  
  List getTopCommenters();
}
