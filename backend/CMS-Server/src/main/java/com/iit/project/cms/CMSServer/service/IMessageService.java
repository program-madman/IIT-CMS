package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dto.MarkMessageAsReadRequest;
import com.iit.project.cms.CMSServer.dto.SendMsgRequest;

public interface IMessageService {
    BaseResponse sendMessageToDept(SendMsgRequest request);

    BaseResponse sendMessageToUser(SendMsgRequest request);

    BaseResponse sendMessageToUsers(SendMsgRequest request);

    BaseResponse getAllMessages(Long uid);
    BaseResponse getAllMessagesOneSql(Long uid);

    BaseResponse getAllMessagesSendByMe(Long uid);

    BaseResponse getAllMessagesSendToMe(Long uid);

    BaseResponse markMessageAsRead(Long uid, MarkMessageAsReadRequest request);
}
