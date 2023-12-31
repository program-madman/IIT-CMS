package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.MarkMessageAsReadRequest;
import com.iit.project.cms.CMSServer.dto.SendMsgRequest;
import com.iit.project.cms.CMSServer.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.iit.project.cms.CMSServer.common.Symbol.KEY_HEADER_USER_ID;

@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @RequestMapping(value = "/sendMessageToDept")
    public BaseResponse sendMessageToDept(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody SendMsgRequest request) {
        request.setFromUserId(Long.parseLong(uid));
        return messageService.sendMessageToDept(request);
    }

    @RequestMapping(value = "/sendMessageToUser")
    public BaseResponse sendMessageToUser(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody SendMsgRequest request) {
        request.setFromUserId(Long.parseLong(uid));
        return messageService.sendMessageToUser(request);
    }

    @RequestMapping(value = "/sendMessageToUsers")
    public BaseResponse sendMessageToUsers(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody SendMsgRequest request) {
        request.setFromUserId(Long.parseLong(uid));
        return messageService.sendMessageToUsers(request);
    }

    @RequestMapping(value = "/getAllMessages")
    public BaseResponse getAllMessages(@RequestHeader(KEY_HEADER_USER_ID) String uid) {
        return messageService.getAllMessages(Long.parseLong(uid));
    }

    @RequestMapping(value = "/getAllMessagesSendByMe")
    public BaseResponse getAllMessagesSendByMe(@RequestHeader(KEY_HEADER_USER_ID) String uid) {
        return messageService.getAllMessagesSendByMe(Long.parseLong(uid));
    }

    @RequestMapping(value = "/getAllMessagesSendToMe")
    public BaseResponse getAllMessagesSendToMe(@RequestHeader(KEY_HEADER_USER_ID) String uid) {
        return messageService.getAllMessagesSendToMe(Long.parseLong(uid));
    }

    @RequestMapping(value = "/markMessageAsRead")
    public BaseResponse markMessageAsRead(@RequestHeader(KEY_HEADER_USER_ID) String uid, @RequestBody MarkMessageAsReadRequest request) {
        return messageService.markMessageAsRead(Long.parseLong(uid), request);
    }
}
