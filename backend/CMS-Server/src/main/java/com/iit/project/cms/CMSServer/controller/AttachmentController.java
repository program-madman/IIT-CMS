package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.service.IAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.iit.project.cms.CMSServer.common.Symbol.KEY_HEADER_USER_ID;

@RestController
@Slf4j
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private IAttachmentService attachmentService;

    @RequestMapping(value = "/upload")
    public BaseResponse upload(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestParam("file") MultipartFile file) {
        return attachmentService.upload(uid, file);
    }
}
