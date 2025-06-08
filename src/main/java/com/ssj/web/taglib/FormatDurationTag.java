package com.ssj.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Formats an int duration in milliseconds as "mm:ss".
 */
public class FormatDurationTag extends SimpleTagSupport {

  private int duration;

  @Override
  public void doTag() throws JspException, IOException {
    PageContext pageContext = (PageContext) getJspContext();

    JspWriter out = pageContext.getOut();

    String minutes = "0";
    if (duration > 60000) {
      minutes = new DecimalFormat("#0").format(duration / 60000);
    }
    String seconds = new DecimalFormat("00").format((duration % 60000) / 1000);

    try {
      out.print(minutes);
      out.print(":");
      out.print(seconds);

    } catch (Exception e) {
       // Ignore.
    }
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
