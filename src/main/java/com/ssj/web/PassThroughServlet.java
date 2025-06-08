package com.ssj.web;

import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Servlet for handling static content. Set up as first loaded servlet in web.xml and route static content path(s) to it.
 * Implemented manually like this so that it would be portable (rather than using the default servlet implementation
 * of a specific container).
 *
 * @author sam
 * @version $Id$
 */
public class PassThroughServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String requestURI = request.getRequestURI();

    String requestPath = requestURI.substring(request.getContextPath().length());

    // handle case when URL contains ';jsessionid=oaisjdoaisjd' for some reason
    int indexOfSemicolon = requestPath.indexOf(';');
    if (indexOfSemicolon > -1) {
      requestPath = requestPath.substring(0, indexOfSemicolon);
    }

    String realPath = getServletContext().getRealPath(requestPath);

    if (realPath != null) {
      File realFile = new File(realPath);

      try {
        FileInputStream realFileInputStream = new FileInputStream(realFile);

        FileCopyUtils.copy(realFileInputStream, response.getOutputStream());
      } catch (FileNotFoundException e) {
        // return 404
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
    }

    // return 404
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }
}
