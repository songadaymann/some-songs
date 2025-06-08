package com.ssj.web;

import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

/**
 * Wraps a Spring ContextLoaderListener instance, and only passes the
 * context initialized/destroyed events through if the app has been
 * configured with appropriate database properties. Also handles saving
 * the database properties on context destroy if a system property has
 * been set from the database config JSP.
 *
 * @author sam
 * @version $Id$
 */
public class ContextListener implements ServletContextListener {

  private boolean springContextInitialized = false;

  private ContextLoaderListener listener = new ContextLoaderListener();

  private SomeSongsProperties dbConfig = SomeSongsProperties.getInstance();

  public void contextInitialized(ServletContextEvent event) {

//    dbConfig.loadResource(event.getServletContext());
    dbConfig.loadResource();

    if (dbConfig.isConfigured()) {
      try {
        listener.contextInitialized(event);
        springContextInitialized = true;
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }

  public void contextDestroyed(ServletContextEvent event) {
    if (springContextInitialized) {
      try {
        listener.contextDestroyed(event);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }        
    } else if (dbConfig.isSaveOnContextDestroy()) {
      dbConfig.setConfigured();
      dbConfig.save();
    }
  }
}
