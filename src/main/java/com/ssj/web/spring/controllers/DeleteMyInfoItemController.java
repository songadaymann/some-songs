package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.model.user.User;
import com.ssj.model.artist.Artist;
import com.ssj.model.song.Song;
import com.ssj.model.song.search.SongSearch;
import com.ssj.model.playlist.Playlist;
import com.ssj.service.user.UserService;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongSearchService;
import com.ssj.service.playlist.PlaylistService;

@Controller
public class DeleteMyInfoItemController {
  private UserService userService;
  private ArtistService artistService;
  private SongService songService;
  private SongSearchService songSearchService;
  private PlaylistService playlistService;

  @RequestMapping("/user/delete_item")
  protected ModelAndView handleRequestInternal(@RequestParam("itemId") String myInfoItemId) {
    Map<String, Object> model = new HashMap<String, Object>();

    if (myInfoItemId != null) {
      User user = SecurityUtil.getUser();
      int itemId = Integer.parseInt(myInfoItemId.substring(2));
      if (myInfoItemId.startsWith("fs")) {
        // delete favorite song by id
        Song song = songService.getSong(itemId);
        if (song != null) {
          userService.toggleFavoriteSong(user, song);
        } else {
          model.put("error", "Could not find artist song id " + itemId);
        }
      } else if (myInfoItemId.startsWith("fa")) {
        // delete favorite artist by id
        Artist artist = artistService.getArtist(itemId);
        if (artist != null) {
          userService.toggleFavoriteArtist(user, artist);
        } else {
          model.put("error", "Could not find artist with id " + itemId);
        }
      } else if (myInfoItemId.startsWith("fp")) {
        // delete favorite playlist by id
        Playlist playlist = playlistService.getPlaylist(itemId);
        if (playlist != null) {
          userService.toggleFavoritePlaylist(user, playlist);
        } else {
          model.put("error", "Could not find playlist with id " + itemId);
        }
      } else if (myInfoItemId.startsWith("pu")) {
        // delete preferred user
        User preferredUser = userService.getUser(itemId);
        if (preferredUser != null) {
          userService.togglePreferredUser(user, preferredUser);
        } else {
          model.put("error", "Could not find user with id " + itemId);
        }
      } else if (myInfoItemId.startsWith("iu")) {
        // delete ignored user
        User ignoredUser = userService.getUser(itemId);
        if (ignoredUser != null) {
          userService.toggleIgnoredUser(user, ignoredUser);
        } else {
          model.put("error", "Could not find user with id " + itemId);
        }
      } else if (myInfoItemId.startsWith("ss")) {
        SongSearch search = songSearchService.getSongSearch(itemId);
        if (search != null) {
          if (search.getUser().getId() == user.getId()) {
            songSearchService.deleteSearch(search);
          } else {
            model.put("error", "You can only delete your own saved searches");
          }
        } else {
          model.put("error", "Could not find saved search with id = " + itemId);
        }
      } else if (myInfoItemId.startsWith("pl")) {
        Playlist playlist = playlistService.getPlaylist(itemId);
        if (playlist != null) {
          if (playlist.getUser().getId() == user.getId()) {
            playlistService.delete(playlist);
          } else {
            model.put("error", "You can only delete your own playlists");
          }
        } else {
          model.put("error", "Could not find playlist with id = " + itemId);
        }
      }
    } else {
      model.put("error", "You must be logged in to delete a favorite song or artist, or a preferred or ignored user");
    }
    if (model.isEmpty()) {
      model.put("success", "true");
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setSongSearchService(SongSearchService songSearchService) {
    this.songSearchService = songSearchService;
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }
}
