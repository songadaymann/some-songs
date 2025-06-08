package com.ssj.dao.playlist;

import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.song.Song;
import com.ssj.dao.SearchDAO;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistDAO extends SearchDAO<Playlist, PlaylistSearch> {

  public int countPlaylists(User user);

  public List getPlaylists(User user, int numPlaylists);

  public void addToPlaylist(Song song, Playlist playlist);

  public Playlist getWithItems(int playlistId);

  public void updateItemCount(Playlist playlist);

  public void updateRatingInfo(Playlist playlist);

  public Playlist getNewerPlaylist(int id);

  public Playlist getOlderPlaylist(int id);

  public Playlist getHigherRatedPlaylist(int id);

  public Playlist getLowerRatedPlaylist(int id);
}
