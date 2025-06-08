package com.ssj.web.spring.controllers;

import com.ssj.model.song.BrokenLinkReport;
import com.ssj.model.song.Song;
import com.ssj.service.song.SongService;
import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private SongService songService;

  @RequestMapping("/broken_link_reports")
  public ModelMap newBrokenLinkReportsHandler(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "25") int pageSize) {

    List<BrokenLinkReport> reports = null;
    try {
      reports = songService.getNewBrokenLinkReports(start, pageSize);
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModelMap model = new ModelMap();
    model.addAttribute("reports", reports);
    model.addAttribute("start", start);
    model.addAttribute("pageSize", pageSize);

    return model;
  }

  @RequestMapping("/hide_song")
  public ModelAndView hideSongHandler(@RequestParam("id") int songId) {
    Map model = new HashMap();
    Song song = songService.getSong(songId);
    if (song == null) {
      model.put("error", "Could not find song");
    } else {
      song.setShowSong(false);
      songService.save(song);
      model.put("success", "true");
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @RequestMapping("/process_broken_link_report")
  public ModelAndView processBrokenLinkReport(@RequestParam("id") int reportId) {
    Map model = new HashMap();
    BrokenLinkReport report = songService.getBrokenLinkReport(reportId);
    if (report == null) {
      model.put("error", "Could not find report");
    } else {
      report.setProcessedDate(new Date());
      songService.saveBrokenLinkReport(report);
      model.put("success", "true");
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}
