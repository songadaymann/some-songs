package com.ssj.web.taglib;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.ServletContext;

import com.ssj.web.util.HTMLUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class EscapeSongInfoTag extends SimpleTagSupport {

  private String content;

  public void doTag() throws JspException {

    PageContext pageContext = (PageContext) getJspContext();
    ServletContext servletContext = pageContext.getServletContext();
    WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

    HTMLUtil htmlUtil = (HTMLUtil) appContext.getBean("htmlUtil");

    JspWriter out = pageContext.getOut();

    try {

      out.print(htmlUtil.escapeSongInfo(content));

    } catch (Exception e) {
       // Ignore.
    }
  }

  public void setContent(String content) {
    this.content = content;
  }
}