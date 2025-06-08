package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.song.SongSearchService;
import com.ssj.service.song.SongException;
import com.ssj.model.song.search.SongSearch;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class SaveSearchController {
  private SongSearchService songSearchService;

  @RequestMapping("/user/save_search")
  protected ModelAndView handleRequestInternal(WebRequest request,
                                               @RequestParam("searchName") String searchName) {
    SongSearch search = (SongSearch) request.getAttribute("songSearch", WebRequest.SCOPE_SESSION);

    if (search == null) {
      throw new SongException("Could not find search to save, please perform a search first");
    }

    Map<String, String> jsonModel = new HashMap<String, String>(1);
    if (searchName.trim().length() < 3 || searchName.trim().length() > 128) {
      jsonModel.put("error", "Search name must be 4 to 128 characters long");
    } else {
      searchName = searchName.trim();
      // TODO escape HTML in name?
      try {
        User user = SecurityUtil.getUser();

        search.setUser(user);
        search.setName(searchName);

        songSearchService.saveSearch(search);
        jsonModel.put("success", "true");
      } catch (Exception e) {
        e.printStackTrace();
        jsonModel.put("error", e.getMessage());
      }
    }

    return new ModelAndView(new JSONView(), "jsonModel", jsonModel);
  }

  @Autowired
  public void setSongSearchService(SongSearchService songSearchService) {
    this.songSearchService = songSearchService;
  }
}
