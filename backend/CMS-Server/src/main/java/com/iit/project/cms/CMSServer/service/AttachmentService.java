package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.iit.project.cms.CMSServer.common.Symbol.FILE_UPLOAD_DIR;

@Service
public class AttachmentService implements IAttachmentService {
    private static final String UPLOAD_DIR = "../cms_uploads/";

    @Override
    public BaseResponse upload(String userId, MultipartFile file) {
        if (file.isEmpty()) {
            return BaseResponse.error("Please select a file to upload");
        }
        try {
            byte[] bytes = file.getBytes();
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            return BaseResponse.success();
        } catch (IOException e) {
            return BaseResponse.error("Error uploading file");
        }
    }

    @Override
    public BaseResponse download() {
//        @GetMapping("/download/{filename:.+}")
//        public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//            try {
//                Path path = Paths.get(UPLOAD_DIR + filename);
//                Resource resource = new UrlResource(path.toUri());
//
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            } catch (MalformedURLException e) {
//                return ResponseEntity.notFound().build();
//            }
//        }
        return null;
    }
}
