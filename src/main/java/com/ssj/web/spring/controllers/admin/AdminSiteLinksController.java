package com.ssj.web.spring.controllers.admin;

import com.ssj.model.content.SiteLink;
import com.ssj.service.content.SiteLinkService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminSiteLinksController {

  private static final Logger LOGGER = Logger.getLogger(AdminSiteLinksController.class);

  private SiteLinkService siteLinkService;

  @RequestMapping(value = "/admin/site_links", method = RequestMethod.GET, params = "clearCache=true")
  public ModelAndView clearSiteLinksCache() {
    siteLinkService.clearCache();
    return new ModelAndView("redirect:/admin/site_links", "cleared", "true");
  }

  @RequestMapping(value = "/admin/site_links", method = RequestMethod.GET)
  public ModelAndView manageSiteLinks() {
    ModelAndView modelAndView = new ModelAndView("admin/site_links");

    List<SiteLink> siteLinks = siteLinkService.getAll();

    modelAndView.addObject("siteLinks", siteLinks);

    return modelAndView;
  }

  
  @RequestMapping(value = "/admin/site_link", method = RequestMethod.GET)
  public ModelAndView editSiteLink(@RequestParam(value = "id", required = false) Integer id) {
    ModelAndView modelAndView = new ModelAndView("admin/site_link");

    SiteLink siteLink = null;
    
    if (id != null) {
      siteLink = siteLinkService.get(id);
    }

    if (siteLink == null) {
      siteLink = new SiteLink();
    }

    modelAndView.addObject("siteLink", siteLink);

    return modelAndView;
  }
  
  @RequestMapping(value = "/admin/site_link", method = RequestMethod.POST)
  public ModelAndView updateSiteLink(@ModelAttribute("siteLink") @Valid SiteLink siteLink, BindingResult errors) {
    ModelAndView modelAndView = null;
    if (!errors.hasErrors()) {
      try {
        siteLinkService.save(siteLink);
        modelAndView = new ModelAndView("redirect:/admin/site_links", "saved", "true");
      } catch (Exception e) {
        LOGGER.error(e);
        errors.reject("error.siteLink", e.getMessage());
      }
    }
    if (modelAndView == null) {
      modelAndView = new ModelAndView("admin/site_link", errors.getModel());
    }
    return modelAndView;
  }
  
  @RequestMapping("/admin/delete_site_link")
  public ModelAndView deleteSiteLink(@RequestParam("id") int id) {
    siteLinkService.delete(id);
    return new ModelAndView("redirect:/admin/site_links", "deleted", "true");
  }

  @Autowired
  public void setSiteLinkService(SiteLinkService siteLinkService) {
    this.siteLinkService = siteLinkService;
  }
}
