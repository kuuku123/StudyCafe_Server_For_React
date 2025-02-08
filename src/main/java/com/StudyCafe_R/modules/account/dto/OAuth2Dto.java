package com.StudyCafe_R.modules.account.dto;

import java.util.Map;
import lombok.Data;

@Data
public class OAuth2Dto {

  private String provider;
  private Map<String, String> attributes;

  public String getAttribute(String key) {
    return attributes.get(key);
  }

}
