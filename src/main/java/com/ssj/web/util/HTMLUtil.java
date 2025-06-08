package com.ssj.web.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Set;
import java.util.HashSet;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class HTMLUtil {

  private static final Pattern OPENING_TAGS_PATTERN = Pattern.compile("<([a-zA-Z]*)([\\s\\w=\\\"']*)>");
  private static final Pattern CLOSING_TAGS_PATTERN = Pattern.compile("</([a-zA-Z]*)>");

  private static final String OPENING_TAG_REPLACE = "&lt;$1$2&gt;";
  private static final String CLOSING_TAG_REPLACE = "&lt;/$1&gt;";

  private static final String WHITESPACE_REGEX = "(\\r\\n)|\\r|\\n";
  private static final String WHITESPACE_REPLACE = "<br>";

//  private static final String JAVASCRIPT_HREF_REGEX = "href=([\\\"']?)*\\s*javascript:";
  private static final Pattern JAVASCRIPT_HREF_PATTERN = Pattern.compile("href=([\\\"']?)*\\s*javascript:", Pattern.CASE_INSENSITIVE);
  private static final String JAVASCRIPT_HREF_REPLACE = "href=$1";

  private static final Pattern EVENT_HANDLER_PATTERN = Pattern.compile("on[a-zA-Z]*=", Pattern.CASE_INSENSITIVE);
  private static final String EVENT_HANDLER_REPLACE = "";

  private Set messageBoardTags = new HashSet();

  private Set commentTags = new HashSet();

  private Set songInfoTags = new HashSet();

  private Set songMoreInfoTags = new HashSet();

  public String escapeMessageBoardContent(String messageBoardContent) {
    return escapeTags(messageBoardContent, messageBoardTags);
  }

  public String escapeCommentContent(String commentContent) {
    return escapeTags(commentContent, commentTags);
  }

  public String escapeSongInfo(String songInfo) {
    return escapeTags(songInfo, songInfoTags);
  }

  public String escapeSongMoreInfo(String songMoreInfo) {
    return escapeTags(songMoreInfo, songMoreInfoTags);
  }

  String escapeTags(String content, Set allowedTags) {
    StringBuffer sb = new StringBuffer();

    escapeOpeningTags(sb, content, allowedTags);

    content = sb.toString();
    sb = new StringBuffer();

    escapeClosingTags(sb, content, allowedTags);

    String out = replaceWhiteSpace(sb.toString());

    out = removeJavaScriptFromLinks(out);

    out = removeEventHandlers(out);

    return out;
  }

  void escapeOpeningTags(StringBuffer sb, String content, Set allowedTags) {
    escapeTags(sb, content, OPENING_TAGS_PATTERN, allowedTags, OPENING_TAG_REPLACE);
  }

  void escapeClosingTags(StringBuffer sb, String content, Set allowedTags) {
    escapeTags(sb, content, CLOSING_TAGS_PATTERN, allowedTags, CLOSING_TAG_REPLACE);
  }

  void escapeTags(StringBuffer sb, String content, Pattern tagPattern, Set allowedTags, String replace) {
    Matcher m = tagPattern.matcher(content);

    while (m.find()) {
      String tag = m.group(1);
      if (tag == null || !allowedTags.contains(tag)) {
        m.appendReplacement(sb, replace);
      }
    }
    m.appendTail(sb);
  }

  public String replaceWhiteSpace(String content) {
    return content.replaceAll(WHITESPACE_REGEX, WHITESPACE_REPLACE);
  }

  public String removeJavaScriptFromLinks(String content) {
    return JAVASCRIPT_HREF_PATTERN.matcher(content).replaceAll(JAVASCRIPT_HREF_REPLACE);
//    return content.replaceAll(JAVASCRIPT_HREF_REGEX, JAVASCRIPT_HREF_REPLACE);
  }

  public String removeEventHandlers(String content) {
    return EVENT_HANDLER_PATTERN.matcher(content).replaceAll(EVENT_HANDLER_REPLACE);
  }

  public void setMessageBoardTags(Set messageBoardTags) {
    this.messageBoardTags = messageBoardTags;
  }

  public void setCommentTags(Set commentTags) {
    this.commentTags = commentTags;
  }

  public void setSongInfoTags(Set songInfoTags) {
    this.songInfoTags = songInfoTags;
  }

  public void setSongMoreInfoTags(Set songMoreInfoTags) {
    this.songMoreInfoTags = songMoreInfoTags;
  }
}
