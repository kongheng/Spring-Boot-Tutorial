package com.kongheng.springboot.controller;

import com.kongheng.springboot.entity.Attachment;
import com.kongheng.springboot.model.ResponseData;
import com.kongheng.springboot.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AttachmentController {

  @Autowired
  private AttachmentService attachmentService;

  @PostMapping("/upload")
  public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
    Attachment attachment = attachmentService.saveAttachment(file);
    String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/download/")
        .path(attachment.getId())
        .toUriString();
    return ResponseData.builder()
        .downloadURL(downloadURL)
        .fileType(file.getContentType())
        .fileSize(file.getSize())
        .build();
  }

  @GetMapping("/download/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
    Attachment attachment = attachmentService.getAttachment(fileId);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(attachment.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + attachment.getFileName() + "\"")
        .body(new ByteArrayResource(attachment.getData()));
  }
}
