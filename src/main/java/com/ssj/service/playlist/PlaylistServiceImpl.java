package com.ssj.service.playlist;

import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistItem;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.song.Song;
import com.ssj.dao.playlist.PlaylistDAO;
import com.ssj.dao.playlist.PlaylistItemDAO;
import com.ssj.service.song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
@Transactional(readOnly = true)
public class PlaylistServiceImpl
implements   PlaylistService, ApplicationListener<PlaylistRatingEvent> {

  private PlaylistDAO playlistDAO;
  private PlaylistItemDAO playlistItemDAO;

  private SongService songService;

  public int countPlaylists(User user) {
    return playlistDAO.countPlaylists(user);
  }

  public List getPlaylists(User user, int numPlaylists) {
    return playlistDAO.getPlaylists(user, numPlaylists);
  }

  public Playlist getPlaylist(int playlistId) {
    return playlistDAO.get(playlistId);
  }

  @Transactional(readOnly = false)
  public void delete(Playlist playlist) {
    playlistDAO.delete(playlist);
  }

  @Transactional(readOnly = false)
  public void save(Playlist playlist) {
    playlistDAO.save(playlist);
  }

  @Transactional(readOnly = false)
  public void addToPlaylist(User user, int songId, int playlistId) {
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    Playlist playlist = playlistDAO.get(playlistId);
    if (playlist == null) {
      throw new PlaylistException("No playlist with id " + playlistId);
    }
    if (!canEditPlaylist(playlist, user)) {
      throw new PlaylistException("You do not have permission to edit this playlist");
    }
    Song song = songService.getSong(songId);
    if (song == null) {
      throw new PlaylistException("No song with id " + songId);
    }
    PlaylistItem item = new PlaylistItem();
    item.setSong(song);
    item.setPlaylist(playlist);
    playlist.getItems().add(item);
    playlistItemDAO.save(item);
    playlistDAO.updateItemCount(playlist);
  }

  @Transactional(readOnly = false)
  public void saveNote(User user, int playlistItemId, String note) {
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    PlaylistItem item = playlistItemDAO.get(playlistItemId);
    if (item == null) {
      throw new PlaylistException("No playlist item with id " + playlistItemId);
    }
    if (item.getPlaylist().getUser().getId() != user.getId()) {
      throw new PlaylistException("You do not have permission to edit this playlist item");
    }
    item.setNote(note);
    playlistItemDAO.save(item);
  }

  @Transactional(readOnly = false)
  public void reorderItems(User user, int playlistId, int[] itemIds) {
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    Playlist playlist = playlistDAO.get(playlistId);
    if (playlist == null) {
      throw new PlaylistException("No playlist with id " + playlistId);
    }
    if (playlist.getUser().getId() != user.getId()) {
      throw new PlaylistException("You do not have permission to edit this playlist");
    }
    Set<PlaylistItem> items = playlist.getItems();
    Set<PlaylistItem> tmpItems = new HashSet<PlaylistItem>(items);
    items.clear();
    for (int itemId : itemIds) {
      for (PlaylistItem item : tmpItems) {
        if (item.getId() == itemId) {
          item.setOrdinal(items.size());
          item.setChangeDate(new Date());
          items.add(item);
          break;
        }
      }
    }
    playlist.setChangeDate(new Date());
    playlistDAO.save(playlist);
  }

  public boolean canEditPlaylist(Playlist playlist, User user) {
    // for now only a playlist's creator can edit a playlist, but maybe later they will be editable by others
    return (playlist.getUser() != null && user != null && playlist.getUser().getId() == user.getId());
  }

  @Transactional(readOnly = false)
  public void deleteItem(User user, int playlistItemId) {
    PlaylistItem item = playlistItemDAO.get(playlistItemId);
    if (item == null) {
      throw new PlaylistException("Could not find playlist item with id " + playlistItemId);
    }
    Playlist playlist = item.getPlaylist();
    if (!canEditPlaylist(playlist, user)) {
      throw new PlaylistException("You do not have permission to edit this playlist");
    }
    // do i need this part?
//    playlist.getItems().remove(item);
    playlistItemDAO.delete(item);
    playlistDAO.updateItemCount(playlist);
  }

  public List<Playlist> findPlaylists(PlaylistSearch search) {
    List<Playlist> playlists = null;
    search.setTotalResults(playlistDAO.doCount(search));
    if (search.getTotalResults() > 0) {
      playlists = playlistDAO.doSearch(search);
    }
    return playlists;

  }

  @Transactional(readOnly = false)
  public void updateRatingInfo(Playlist playlist) {
    playlistDAO.updateRatingInfo(playlist);
  }

  public Playlist getRandomPlaylist() {
    PlaylistSearch search = new PlaylistSearch();
    int numPlaylists = playlistDAO.doCount(search);
    int randomIndex = (int) (Math.random() * numPlaylists);
    search.setStartResultNum(randomIndex);
    search.setResultsPerPage(1);
    List<Playlist> results = playlistDAO.doSearch(search);
    return results.get(0);
  }

  public Playlist getNewerPlaylist(int id) {
    return playlistDAO.getNewerPlaylist(id);
  }

  public Playlist getOlderPlaylist(int id) {
    return playlistDAO.getOlderPlaylist(id);
  }

  public Playlist getHigherRatedPlaylist(int id) {
    return playlistDAO.getHigherRatedPlaylist(id);
  }

  public Playlist getLowerRatedPlaylist(int id) {
    return playlistDAO.getLowerRatedPlaylist(id);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setPlaylistDAO(PlaylistDAO playlistDAO) {
    this.playlistDAO = playlistDAO;
  }

  @Autowired
  public void setPlaylistItemDAO(PlaylistItemDAO playlistItemDAO) {
    this.playlistItemDAO = playlistItemDAO;
  }

  @Override
  public void onApplicationEvent(PlaylistRatingEvent event) {
    Playlist playlist = getPlaylist(event.getObjectId());
    updateRatingInfo(playlist);
  }
}
