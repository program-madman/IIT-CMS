package com.iit.project.cms.CMSServer.controller;
import com.iit.project.cms.CMSServer.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iit.project.cms.CMSServer.common.BaseResponse;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @GetMapping("/getUsers")
    public BaseResponse getUsers() {
        return queryService.getUsers();
    }

    @GetMapping("/getUserBrowsedHistory")
    public BaseResponse getUserBrowsedHistory() {
        return queryService.getUserBrowsedHistory();
    }

    @GetMapping("/getUserComments")
    public BaseResponse getUserComments() {
        return queryService.getUserComments();
    }

    @GetMapping("/getMessages")
    public BaseResponse getMessages() {
        return queryService.getMessages();
    }

    @GetMapping("/getOperateHistory")
    public BaseResponse getOperateHistory() {
        return queryService.getOperateHistory();
    }

    @GetMapping("/getTotalUserStatistics")
    public BaseResponse getTotalUserStatistics() {
        return queryService.getTotalUserStatistics();
    }

    @GetMapping("/getArticleReadStatusDetails")
    public BaseResponse getArticleReadStatusDetails() {
        return queryService.getArticleReadStatusDetails();
    }

    @GetMapping("/getAttachmentDetails")
    public BaseResponse getAttachmentDetails() {
        return queryService.getAttachmentDetails();
    }

    @GetMapping("/getLikeDetails")
    public BaseResponse getLikeDetails() {
        return queryService.getLikeDetails();
    }

    @GetMapping("/getArticleStatistics")
    public BaseResponse getArticleStatistics() {
        return queryService.getArticleStatistics();
    }
}
