package com.ssj.service.playlist;

import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.search.PlaylistSearch;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistService {

  public int countPlaylists(User user);

  public List getPlaylists(User user, int numPlaylists);

  public Playlist getPlaylist(int playlistId);

  public void delete(Playlist playlist);

  public void save(Playlist playlist);

  public void addToPlaylist(User user, int songId, int playlistId);

  public void saveNote(User user, int playlistItemId, String note);

  public void reorderItems(User user, int playlistId, int[] itemIds);

  public boolean canEditPlaylist(Playlist playlist, User user);

  public void deleteItem(User user, int playlistItemId);

  public List<Playlist> findPlaylists(PlaylistSearch search);

  public void updateRatingInfo(Playlist playlist);

  public Playlist getRandomPlaylist();

  public Playlist getNewerPlaylist(int id);

  public Playlist getOlderPlaylist(int id);

  public Playlist getHigherRatedPlaylist(int id);

  public Playlist getLowerRatedPlaylist(int id);
}
