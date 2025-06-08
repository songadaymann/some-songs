package com.ssj.web.spring;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;
import java.io.PrintWriter;

import com.ssj.model.song.Song;

/**
 * View for outputting JSON from the model.
 *
 * @author sam
 * @version $Id$
 */
public class M3UView implements View {
  public String getContentType() {
    return "audio/x-mpegurl";
  }

  public void render(Map modelMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

    List songs = (List) modelMap.get("songs");

    response.setContentType(getContentType());

    PrintWriter writer = response.getWriter();

    if (songs != null) {
      for (int i = 0; i < songs.size(); i++) {
        Song song = (Song) songs.get(i);
        writer.write(song.getUrl());
        writer.write("\n");
      }
    } else {
      writer.write("Songs not found");
    }
  }
}