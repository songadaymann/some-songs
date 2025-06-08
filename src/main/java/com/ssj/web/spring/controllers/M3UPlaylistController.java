package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ssj.web.spring.M3UView;
import com.ssj.model.song.Song;
import com.ssj.service.song.SongService;

import java.util.List;
import java.util.ArrayList;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class M3UPlaylistController {
  private SongService songService;

  @RequestMapping("/somesongs_playlist.m3u")
  protected ModelAndView handleRequestInternal(@RequestParam("songId") String[] songIds) {
    List<Song> songs = new ArrayList<Song>();

    for (String songId : songIds) {
      int songIdInt = NumberUtils.toInt(songId, -1);
      if (songIdInt > 0) {
        Song song = songService.getSong(songIdInt);
        if (song != null) {
          // only include songs not from bandcamp
          // don't want to show direct links to bandcamp free mp3s, violates terms of service
          if (song.getBandcampTrackId() == null) {
            songs.add(song);
          }
        }
      }
    }

    return new ModelAndView(new M3UView(), "songs", songs);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}
