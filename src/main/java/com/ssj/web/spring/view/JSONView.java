package com.ssj.web.spring.view;

import flexjson.JSONSerializer;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * View for outputting JSON from the model.
 *
 * @author sam
 * @version $Id$
 */
public class JSONView implements View {

  private List<String> includes;
  private List<String> excludes;

  public String getContentType() {
    return "application/json";
  }

  public void render(Map modelMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

    Map map = (Map) modelMap.get("jsonModel");

    response.setContentType(getContentType());

    PrintWriter writer = response.getWriter();

    JSONSerializer serializer = new JSONSerializer();

    if (includes != null && !includes.isEmpty()) {
      for (String include : includes) {
        serializer.include(include);
      }
    }
    if (excludes != null && !excludes.isEmpty()) {
      for (String exclude : excludes) {
        serializer.exclude(exclude);
      }
    }
    serializer.exclude("*.class");

    String json = serializer.serialize(map);

    writer.write(json);
  }

  public void addIncludes(String... includes) {
    if (this.includes == null) {
      this.includes = new ArrayList<String>();
    }
    this.includes.addAll(Arrays.asList(includes));
  }

  public void addExcludes(String... excludes) {
    if (this.excludes == null) {
      this.excludes = new ArrayList<String>();
    }
    this.excludes.addAll(Arrays.asList(excludes));
  }


}