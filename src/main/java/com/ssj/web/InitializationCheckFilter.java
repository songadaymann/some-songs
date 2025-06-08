package com.ssj.web;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter that forces the user to a database properties config page unfil
 * the user has entered properties that result in a successful database
 * connection and has then subsequently redeployed the context. Once the
 * properties have been configured and the context has been redeployed
 * this filter just calls the next filter in the chain.
 *
 * @author sam
 * @version $Id$
 */
public class InitializationCheckFilter implements Filter {

  private static final Logger LOGGER = Logger.getLogger(InitializationCheckFilter.class);

  private boolean configured;

  public void init(FilterConfig filterConfig) throws ServletException {
    LOGGER.debug("Initializating InitializationCheckFilter");
    configured = SomeSongsProperties.getInstance().isConfigured();
    if (configured) {
      LOGGER.debug("Properties Configured! Will pass through to next filter in chain");
    } else {
      LOGGER.debug("Properties Not Configured: Will forward to database properties JSP");
    }

  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    if (configured) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      request.getRequestDispatcher("/admin/connections.jsp").forward(servletRequest, servletResponse);
    }
  }

  public void destroy() {
    // do nothing
  }
}
