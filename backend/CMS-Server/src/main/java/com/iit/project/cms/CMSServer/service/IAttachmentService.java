package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IAttachmentService {

    BaseResponse upload(String userId, MultipartFile file);

    BaseResponse download();
}
