package com.kongheng.springboot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData {

  private String fileName;
  private String downloadURL;
  private String fileType;
  private Long fileSize;
}
