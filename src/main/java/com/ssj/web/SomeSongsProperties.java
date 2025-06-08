package com.ssj.web;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.util.Properties;
import java.io.IOException;
import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Singleton for managing the somesongs.properties file, used by
 * the context listener and initialization check filter to bootstrap
 * the app with database properties.
 *
 * @author sam
 * @version $Id$
 */
public class SomeSongsProperties {

  private static final Logger LOGGER = Logger.getLogger(SomeSongsProperties.class);

  private static final SomeSongsProperties INSTANCE = new SomeSongsProperties();

  public static SomeSongsProperties getInstance() {
    return INSTANCE;
  }
  private Resource propertiesResource;
  private Properties properties = new Properties();

  private boolean saveOnContextDestroy;

  /**
   * Attempts to load the properties file resource from a path relative
   * to the webapp root ("WEB-INF/classes/somesongs.properties"). Loads
   * the properties from the file into a Properties instance, if the file
   * exists.
   *
//   * @param servletContext the servlet context
   */
  public void loadResource() {
    LOGGER.debug("Loading properties from somesongs.properties");
//    propertiesResource = new ServletContextResource(servletContext, "WEB-INF/classes/somesongs.properties");
    propertiesResource = new ClassPathResource("somesongs.properties");
    try {
      LOGGER.debug("Properties file absolute path = " + propertiesResource.getFile().getAbsolutePath());
    } catch (IOException e) {
      LOGGER.error("Could not get properties File");
      e.printStackTrace();
    }

    if (propertiesResource.exists()) {
      try {
        properties.load(propertiesResource.getInputStream());
        LOGGER.debug("Loaded properties from somesongs.properties");
      } catch (Exception e) {
        LOGGER.error("Error loading properties from somesongs.properties", e);
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns true if the property "configured" has the value of "true" in the properties object.
   *
   * @return if the properties are configured
   */
  public boolean isConfigured() {
    return "true".equals(properties.getProperty("configured"));
  }

  /**
   * Attempts to load the MySQL JDBC driver class, then connect to the database
   * using the given URL, username, and password. If successful, sets the properties
   * into the properties file and sets a flag indicating to the context listener
   * that the properties should be marked as configured then saved to disk on context
   * destroy.
   *
   * @param databaseURL the URL to the MySQL database
   * @param databaseUsername the username to use to log in to the database
   * @param databasePassword the password to use to log in to the database
   * @throws ClassNotFoundException if the MySQL JDBC driver class is not found
   * @throws SQLException if there is a problem connecting with the given info
   */
  public void setAndTestDatabaseProperties(String databaseURL, String databaseUsername, String databasePassword) throws ClassNotFoundException, SQLException {
    java.sql.Connection conn = null;
    try {
      // try to connect using the settings
      LOGGER.debug("Loading database driver ...");
      Class.forName("com.mysql.jdbc.Driver");
      LOGGER.debug("Attempting to connect ...");
      conn = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
      PreparedStatement stmt = conn.prepareStatement("SELECT 1");
      ResultSet rs = stmt.executeQuery();
      rs.next();
      rs.close();
      stmt.close();

      // success!
      LOGGER.debug("Connected successfully! Setting properties ...");

      properties.put("jdbc.driverClassName", "com.mysql.jdbc.Driver");
      properties.put("jdbc.url", databaseURL);
      properties.put("jdbc.username", databaseUsername);
      properties.put("jdbc.password", databasePassword);
      // TODO make connection pool settings configurable
      properties.put("jdbc.pool.maxActive", "20");
      properties.put("jdbc.pool.maxIdle", "20");

      LOGGER.debug("Done!");
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void setAndTestSMTPProperties(String host, String port, String protocol, final String username, final String password,
                                       String authenticate, String startTLS, String debug, String systemFromAddress) throws MessagingException {

    LOGGER.debug("Setting mail properties ...");
    Properties mailProps = new Properties();
//    mailProps.setProperty("mail.transport.protocol", protocol);
//    mailProps.setProperty("mail.smtp.host", host);
//    mailProps.setProperty("mail.smtp.port", port);
    mailProps.setProperty("mail.smtp.auth", authenticate);
    mailProps.setProperty("mail.smtp.starttls.enable", startTLS);
    mailProps.setProperty("mail.debug", debug);

    mailProps.setProperty("mail.smtp.connectiontimeout", "10000");

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setProtocol(protocol);
    mailSender.setHost(host);
    mailSender.setPort(Integer.valueOf(port));
    mailSender.setUsername(username);
    mailSender.setPassword(password);
    mailSender.setJavaMailProperties(mailProps);

/*
    Authenticator authenticator = null;
    if ("true".equals(authenticate)) {
      LOGGER.debug("Creating authenticator ...");
      authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      };
    }

    LOGGER.debug("Getting session ...");
    Session session = Session.getDefaultInstance(mailProps, authenticator);

    LOGGER.debug("Populating message ...");
    MimeMessage testMessage = new MimeMessage(session);
*/
    MimeMessage testMessage = mailSender.createMimeMessage();

    testMessage.setText("This is a test of your e-mail configuration");

    testMessage.setSubject("E-mail configuration test");

    Address systemAddress = new InternetAddress(systemFromAddress);
    testMessage.setFrom(systemAddress);

    testMessage.setRecipient(Message.RecipientType.TO, systemAddress);

    LOGGER.debug("Sending message ...");
//    Transport.send(testMessage);
    mailSender.send(testMessage);

    LOGGER.debug("Test sent successfully! Setting properties ... ");

    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", port);
    properties.put("mail.smtp.protocol", protocol);
    properties.put("mail.smtp.username", username);
    properties.put("mail.smtp.password", password);
    properties.put("mail.smtp.authenticate", authenticate);
    properties.put("mail.smtp.starttls", startTLS);
    properties.put("mail.smtp.debug", debug);
    properties.put("mail.system.from", systemFromAddress);

    LOGGER.debug("Done!");
  }

  /**
   * Sets the "configured" property to "true" in the properties object.
   * Call on context destroy after successfully setting the properties.
   */
  public void setConfigured() {
    properties.put("configured", "true");
  }

  /**
   * Attempts to save the properties to the file resource in the
   * webapp classes directory.
   */
  public void save() {
    try {
      LOGGER.debug("Saving properties to somesongs.properties");
//      String propFileName = propertiesResource.getFilename();
//      File propFile = new File(propFileName);
      File propFile = propertiesResource.getFile();
      if (!propFile.exists()) {
        propFile.createNewFile();
      }
      properties.store(new BufferedOutputStream(new FileOutputStream(propFile)), "SomeSongs properties");
      LOGGER.debug("Done saving properties to somesongs.properties!");
    } catch (Exception e) {
      LOGGER.error("Could not save properties to somesongs.properties", e);
      e.printStackTrace();
    }
  }

  public void saveOnContextDestroy() {
    // tell the context listener to set the "configured" property to true on context close
    LOGGER.debug("Setting system property ...");

    saveOnContextDestroy = true;
  }

  public boolean isSaveOnContextDestroy() {
    return saveOnContextDestroy;
  }

}
