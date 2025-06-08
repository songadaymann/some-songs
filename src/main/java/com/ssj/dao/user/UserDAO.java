package com.ssj.dao.user;

import com.ssj.model.user.User;
import com.ssj.model.artist.Artist;
import com.ssj.model.song.Song;
import com.ssj.model.playlist.Playlist;
import com.ssj.dao.DAO;

import java.util.List;

/**
 * User: sam
 * Date: Mar 6, 2007
 * Time: 9:24:01 AM
 * $Id$
 */
public interface UserDAO extends DAO<User> {

  User findByEmail(String email);

  User findByUsername(String username);

  int countUsers();

  int countLogins(int recentLoginsDays);

  boolean isIgnoredUser(User user, User ignoredUser);

  boolean isPreferredUser(User user, User preferredUser);

  boolean isFavoriteArtist(User user, Artist artist);

  boolean isFavoriteSong(User user, Song song);

  List findAdministrators();

  List findUsersByName(String name);

  boolean isFavoritePlaylist(User user, Playlist playlist);

  void updateLastLoginDateByUsername(String username);

  List getTopRaters(int numRaters);

  List getTopCommenters(int numTopUsers);
}
