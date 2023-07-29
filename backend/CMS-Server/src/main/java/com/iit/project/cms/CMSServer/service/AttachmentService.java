package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.iit.project.cms.CMSServer.common.Symbol.FILE_UPLOAD_DIR;

@Service
public class AttachmentService implements IAttachmentService {

    @Override
    public BaseResponse upload(String userId, MultipartFile file) {
        try {
            // 获取上传文件的原始名称
            String fileName = file.getOriginalFilename();
            // 构建目标文件对象
            File destFile = new File(FILE_UPLOAD_DIR + fileName);
            // 保存文件
            file.transferTo(destFile);
            // 这里可以返回附件的URL，这里暂时返回上传成功的提示
            return BaseResponse.success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseResponse.error("upload failed");
    }

    @Override
    public BaseResponse download() {
        return null;
    }
}
