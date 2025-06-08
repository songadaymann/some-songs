package com.ssj.mail.log4j;

import java.util.Properties;

import javax.mail.*;

import org.apache.log4j.net.SMTPAppender;

/**
 * SMTPAppender that allows overriding the port. Taken from:
 * <p/>
 * http://www.mail-archive.com/log4j-user@logging.apache.org/msg07016.html
 *
 * @author sam
 * @version $Id$
 */
public class BetterSMTPAppender extends SMTPAppender {
  private int SMTPPort;
  private String SMTPProtocol;

  public int getSMTPPort() {
    return this.SMTPPort;
  }

  public void setSMTPPort(int smtpPort) {
    this.SMTPPort = smtpPort;
  }

  public String getSMTPProtocol() {
    return SMTPProtocol;
  }

  public void setSMTPProtocol(String SMTPProtocol) {
    this.SMTPProtocol = SMTPProtocol;
  }

  @Override
  protected Session createSession() {
    Properties props;
    try {
      props = new Properties(System.getProperties());
    } catch (SecurityException ex) {
      props = new Properties();
    }

    String prefix = "mail.smtp";
    if (getSMTPProtocol() != null) {
 	    props.put("mail.transport.protocol", getSMTPProtocol());
	  	prefix = "mail." + getSMTPProtocol();
	  }
    if (getSMTPHost() != null) {
      props.put(prefix + ".host", getSMTPHost());
    }
    if (getSMTPPort() > 0) {
      props.put(prefix + ".port", String.valueOf(SMTPPort));
    }

    Authenticator auth = null;
    if (getSMTPPassword() != null && getSMTPUsername() != null) {
      props.put(prefix + ".auth", "true");
      auth = new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(getSMTPUsername(), getSMTPPassword());
        }
      };
    }
    Session session = Session.getInstance(props, auth);
    if (getSMTPProtocol() != null) {
      session.setProtocolForAddress("rfc822", getSMTPProtocol());
    }
    if (getSMTPDebug()) {
      session.setDebug(getSMTPDebug());
    }
    return session;
  }
}
