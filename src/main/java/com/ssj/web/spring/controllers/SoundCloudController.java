package com.ssj.web.spring.controllers;

import com.ssj.gateway.soundcloud.SoundCloudGateway;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.web.JsonResponse;
import com.ssj.web.spring.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user/soundcloud")
public class SoundCloudController {

  @Autowired private SoundCloudGateway soundCloudGateway;

  @RequestMapping("/song/import")
  public String importForm() {
    return "user/soundcloud/import_song";
  }

  @RequestMapping("/tracks")
  public @ResponseBody JsonResponse getTracks(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "25") int size
  ) {
    JsonResponse response = new JsonResponse();

    User user = SecurityUtil.getUser();

    try {
      List<Song> songs = soundCloudGateway.getSongs(user, page, size);

      response.put("songs", songs);
    } catch (Exception e) {
      e.printStackTrace();
      response.setError("Unable to retrieve tracks from SoundCloud, please try again");
    }

    return response;
  }

  @RequestMapping("/sets")
  public @ResponseBody JsonResponse getSets(
      @RequestParam(required = false, defaultValue = "0") int page
  ) {
    JsonResponse response = new JsonResponse();

    User user = SecurityUtil.getUser();

    try {
      List<Playlist> playlists = soundCloudGateway.getPlaylists(user, page);
      response.put("playlists", playlists);
    } catch (Exception e) {
      e.printStackTrace();
      response.setError("Unable to retrieve tracks from SoundCloud, please try again");
    }

    return response;
  }
}
