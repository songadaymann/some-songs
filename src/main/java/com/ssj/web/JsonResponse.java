package com.ssj.web;

import java.util.HashMap;

public class JsonResponse extends HashMap<String, Object> {

  public JsonResponse() {
    super(2);
    put("success", "true");
  }

  public void setError(String error) {
    put("error", error);
    remove("success");
  }
}
