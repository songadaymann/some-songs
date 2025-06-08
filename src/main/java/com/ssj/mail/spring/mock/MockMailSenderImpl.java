package com.ssj.mail.spring.mock;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
public class MockMailSenderImpl implements MailSender {
  public void send(SimpleMailMessage simpleMailMessage) throws MailException {
    System.err.println(simpleMailMessage.toString());
  }

  public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {
    for (SimpleMailMessage simpleMailMessage : simpleMailMessages) {
      send(simpleMailMessage);
    }
  }
}
