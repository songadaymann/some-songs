package com.ssj.web.util;

import com.ssj.model.song.Song;

// todo make taglib that uses this
public class PathsUtil {

  public static String makePath(Song song) {
    return "/songs/" + song.getTitleForUrl() + "-" + song.getId();
  }

  // todo artist path
}
