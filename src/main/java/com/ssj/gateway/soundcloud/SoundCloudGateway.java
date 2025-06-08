package com.ssj.gateway.soundcloud;

import com.ssj.model.playlist.Playlist;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;

import java.util.List;

public interface SoundCloudGateway {

  List<Song> getSongs(User user, Integer page, Integer pageSize);

  List<Playlist> getPlaylists(User user, Integer page);
}
