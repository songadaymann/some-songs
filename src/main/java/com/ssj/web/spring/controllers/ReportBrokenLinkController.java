package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssj.model.song.Song;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongException;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ReportBrokenLinkController {

  private String viewName = "report_broken_link";

  private SongService songService;

  @RequestMapping("/report_broken_link")
  protected String handleRequestInternal(@RequestParam("songId") int songId) {
    if (songId > 0) {
      Song song = songService.getSong(songId);
      if (song != null) {
        songService.reportBrokenLink(song);
      } else {
        throw new SongException("No song with id " + songId);
      }
    } else {
      throw new SongException("Invalid song id " + songId);
    }
    return getViewName();
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  public String getViewName() {
    return viewName;
  }
}
