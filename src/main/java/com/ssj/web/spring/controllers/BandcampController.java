package com.ssj.web.spring.controllers;

import com.ssj.gateway.bandcamp.BandcampGateway;
import com.ssj.model.song.Song;
import com.ssj.web.spring.view.JSONView;
import org.apache.commons.lang.StringUtils;
import org.bandcamp4j.client.BandcampClient;
import org.bandcamp4j.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/bandcamp")
public class BandcampController {

  private BandcampClient bandcampClient;
  private BandcampGateway bandcampGateway;

  @RequestMapping("/song/import")
  public String importForm() {
    return "user/bandcamp/import_song";
  }

  @RequestMapping("/song/info")
  public ModelAndView copyFromBandcamp(@RequestParam(value = "bandcampUrl") String bandcampUrl) {
    ModelAndView modelAndView = new ModelAndView(new JSONView());

    Map<String, Object> model = new HashMap<String, Object>();

    try {
      Song song = bandcampGateway.getSong(bandcampUrl);
      model.put("song", song);
      model.put("success", "true");
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }

    modelAndView.addObject("jsonModel", model);

    return modelAndView;
  }

  @RequestMapping("/artist/albums")
  public ModelAndView getBandcampArtistAlbums(
      @RequestParam(value = "bandcampUrl", required = false) String bandcampUrl
  ) {
    ModelAndView modelAndView = new ModelAndView(new JSONView());

    Map<String, String> model = new HashMap<String, String>();

    if (StringUtils.isNotBlank(bandcampUrl)) {
      UrlInfo urlInfo = null;
      try {
        urlInfo = bandcampClient.urlInfo(bandcampUrl);
      } catch (Exception e) {
        model.put("error", "Error retrieving Bandcamp url info");
      }
      if (urlInfo != null) {
        if (urlInfo.getBandId() != null) {
          Map<Long, Discography> discographies = null;
          try {
            discographies = bandcampClient.bandDiscography(urlInfo.getBandId());
          } catch (Exception e) {
            model.put("error", "Error retrieving Bandcamp discography");
          }
          if (discographies != null) {
            Discography discography = discographies.get(urlInfo.getBandId());
            if (discography != null) {
              List<Album> albums = discography.getAlbums();
              if (albums != null) {
                model.put("success", "true");
                model.put("albumCount", Integer.toString(albums.size()));
                for (int i = 0; i < albums.size(); i++) {
                  Album album = albums.get(i);
                  model.put("albumName"+i, album.getTitle());
                  model.put("albumId"+i, Long.toString(album.getAlbumId()));
                }
              } else {
                model.put("error", "No albums found for this band");
              }
            } else {
              model.put("error", "Could not find discography for band");
            }
          } else {
            model.put("error", "Could not find discogpraphies for bands");
          }
        } else {
          model.put("error", "Bandcamp did not return a band id, please check your URL");
        }
      }
    } else {
      model.put("error", "Please provide the song's Bandcamp URL");
    }

    modelAndView.addObject("jsonModel", model);

    return modelAndView;
  }

  @Autowired
  public void setBandcampClient(BandcampClient bandcampClient) {
    this.bandcampClient = bandcampClient;
  }

  @Autowired
  public void setBandcampGateway(BandcampGateway bandcampGateway) {
    this.bandcampGateway = bandcampGateway;
  }
}
