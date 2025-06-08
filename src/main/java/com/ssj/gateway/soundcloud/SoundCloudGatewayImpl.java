package com.ssj.gateway.soundcloud;

import com.ssj.model.playlist.Playlist;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.service.user.SocialUserService;
import com.ssj.service.user.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.social.soundcloud.api.SoundCloud;
import org.springframework.social.soundcloud.api.Track;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SoundCloudGatewayImpl implements SoundCloudGateway {
  public static final int PAGE_SIZE = 20;

  @Autowired private SocialUserService socialUserService;

  @Override
  public List<Song> getSongs(User user, Integer page, Integer pageSize) {
    SoundCloud soundCloud = socialUserService.getApi(user, SoundCloud.class);

    if (soundCloud == null) {
      throw new UserException("User has not connected a SoundCloud account");
    }

    int pageInt = page == null ? 0 : page;
    int pageSizeInt = pageSize == null || pageSize <= 0 ? PAGE_SIZE : pageSize;
    final Pageable pageable = new PageRequest(pageInt, pageSizeInt);

    Page<Track> tracks = soundCloud.meOperations().getTracks(pageable);

    return tracksToSongs(tracks);
  }

  private List<Song> tracksToSongs(Page<Track> tracks) {
    List<Song> songs = new ArrayList<Song>(tracks.getNumberOfElements());
    for (Track track : tracks) {
      if (!track.isStreamable()) continue;

      Song song = trackToSong(track);
      songs.add(song);
    }
    return songs;
  }

  private Song trackToSong(Track track) {
    Song song = new Song();
    song.setTitle(track.getTitle());
    song.setInfo(track.getDescription());
    song.setDuration(track.getDuration());
    song.setSoundCloudUrl(track.getPermalinkUrl());
    song.setSoundCloudTrackId(Long.valueOf(track.getId()));
    song.setUrl(track.getStreamUrl());
    return song;
  }

  @Override
  public List<Playlist> getPlaylists(User user, Integer page) {
    SoundCloud soundCloud = socialUserService.getApi(user, SoundCloud.class);

    if (soundCloud == null) {
      throw new UserException("User has not connected a SoundCloud account");
    }

    int pageInt = page == null ? 0 : page;
    final Pageable pageable = new PageRequest(pageInt, PAGE_SIZE);

    Page<org.springframework.social.soundcloud.api.Playlist> sets =
        soundCloud.meOperations().getPlaylists(pageable);

    return setsToPlaylists(sets);
  }

  private List<Playlist> setsToPlaylists(Page<org.springframework.social.soundcloud.api.Playlist> sets) {
    List<Playlist> playlists = new ArrayList<Playlist>(sets.getNumberOfElements());
    for (org.springframework.social.soundcloud.api.Playlist set : sets) {
      Playlist playlist = setToPlaylist(set);
      playlists.add(playlist);
    }
    return playlists;
  }

  private Playlist setToPlaylist(org.springframework.social.soundcloud.api.Playlist set) {
    Playlist playlist = new Playlist();
    playlist.setTitle(set.getTitle());
    playlist.setDescription(set.getDescription());
    return playlist;
  }
}
