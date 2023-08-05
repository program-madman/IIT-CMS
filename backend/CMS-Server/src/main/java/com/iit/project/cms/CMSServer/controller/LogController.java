package com.iit.project.cms.CMSServer.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.iit.project.cms.CMSServer.common.Symbol.LOG_FILE;

@RestController
@Slf4j
public class LogController {

    @GetMapping("/getLog")
    @CrossOrigin(origins = "*")
    public String getLog() throws IOException {
        // 读取log文件的内容并返回
        byte[] content = Files.readAllBytes(Paths.get(LOG_FILE));
        String s = new String(content);
        log.info(s);
        return s;
    }
}

