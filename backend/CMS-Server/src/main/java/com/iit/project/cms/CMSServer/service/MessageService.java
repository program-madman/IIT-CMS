package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.DepartmentRepository;
import com.iit.project.cms.CMSServer.dao.MessageReadStatusRepository;
import com.iit.project.cms.CMSServer.dao.MessageRepository;
import com.iit.project.cms.CMSServer.dao.UserRepository;
import com.iit.project.cms.CMSServer.dto.GetMessageResponse;
import com.iit.project.cms.CMSServer.dto.GetUserInfoResponse;
import com.iit.project.cms.CMSServer.dto.MarkMessageAsReadRequest;
import com.iit.project.cms.CMSServer.dto.SendMsgRequest;
import com.iit.project.cms.CMSServer.entity.Message;
import com.iit.project.cms.CMSServer.entity.MessageReadStatus;
import com.iit.project.cms.CMSServer.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageService implements IMessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageReadStatusRepository messageReadStatusRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public BaseResponse getAllMessages(Long uid) {
        List<Message> allMessages = messageRepository.getAllMessages();
        List<GetMessageResponse> responseList = new ArrayList<>();
        for (Message message : allMessages) {
            GetMessageResponse r = new GetMessageResponse();
            BeanUtils.copyProperties(message, r);
            User fromUser = userRepository.getUserById(message.getFromUser());
            GetUserInfoResponse fui = new GetUserInfoResponse();
            BeanUtils.copyProperties(fromUser,fui);
            r.setFromUserInfo(fui);
            r.setFromUserName(fromUser.getFirstName() + " " + fromUser.getLastName());
            User toUser = userRepository.getUserById(message.getToUser());
            GetUserInfoResponse tui = new GetUserInfoResponse();
            BeanUtils.copyProperties(toUser,tui);
            r.setToUserInfo(tui);
            r.setToUserName(toUser.getFirstName() + " " + toUser.getLastName());
            responseList.add(r);
        }
        return BaseResponse.success(responseList);
    }

    @Override
    public BaseResponse getAllMessagesOneSql(Long uid) {
        List<GetMessageResponse> allMessages = messageRepository.getAllMessagesOneSql();
        return BaseResponse.success(allMessages);
    }

    @Override
    public BaseResponse getAllMessagesSendByMe(Long uid) {
        List<Message> messagesSentByUser = messageRepository.getMessagesSentByUser(uid);
        List<GetMessageResponse> responseList = new ArrayList<>();
        for (Message message : messagesSentByUser) {
            GetMessageResponse r = new GetMessageResponse();
            BeanUtils.copyProperties(message, r);
            User fromUser = userRepository.getUserById(message.getFromUser());
            GetUserInfoResponse fui = new GetUserInfoResponse();
            BeanUtils.copyProperties(fromUser,fui);
            r.setFromUserInfo(fui);
            r.setFromUserName(fromUser.getFirstName() + " " + fromUser.getLastName());
            User toUser = userRepository.getUserById(message.getToUser());
            GetUserInfoResponse tui = new GetUserInfoResponse();
            BeanUtils.copyProperties(toUser,tui);
            r.setToUserInfo(tui);
            r.setToUserName(toUser.getFirstName() + " " + toUser.getLastName());
            responseList.add(r);
        }
        return BaseResponse.success(responseList);
    }

    @Override
    public BaseResponse getAllMessagesSendToMe(Long uid) {
        List<Message> messagesSentToUser = messageRepository.getMessagesSentToUser(uid);
        List<GetMessageResponse> responseList = new ArrayList<>();
        for (Message message : messagesSentToUser) {
            GetMessageResponse r = new GetMessageResponse();
            BeanUtils.copyProperties(message, r);
            MessageReadStatus isRead = messageReadStatusRepository.getMessageReadStatusById(r.getMessageId(), uid);
            if (ObjectUtils.isEmpty(isRead)) {
                r.setIsRead(false);
            } else {
                r.setIsRead(true);
            }
            User fromUser = userRepository.getUserById(message.getFromUser());
            GetUserInfoResponse fui = new GetUserInfoResponse();
            BeanUtils.copyProperties(fromUser,fui);
            r.setFromUserInfo(fui);
            r.setFromUserName(fromUser.getFirstName() + " " + fromUser.getLastName());
            User toUser = userRepository.getUserById(message.getToUser());
            GetUserInfoResponse tui = new GetUserInfoResponse();
            BeanUtils.copyProperties(toUser,tui);
            r.setToUserInfo(tui);
            r.setToUserName(toUser.getFirstName() + " " + toUser.getLastName());
            responseList.add(r);
        }
        return BaseResponse.success(responseList);
    }

    @Override
    public BaseResponse markMessageAsRead(Long uid, MarkMessageAsReadRequest request) {
        MessageReadStatus messageReadStatus = new MessageReadStatus();
        messageReadStatus.setMessageId(request.getMessageId());
        messageReadStatus.setUserId(uid);
        if (!ObjectUtils.isEmpty(messageReadStatusRepository.getMessageReadStatusById(request.getMessageId(), uid))) {
            return BaseResponse.error("Message is already read, message id: " + messageReadStatus.getMessageId());
        }

        if (messageReadStatusRepository.createMessageReadStatus(messageReadStatus)){
            return BaseResponse.success();
        } else {
            return BaseResponse.error("Mark message as read failed, the message id is : " + messageReadStatus.getMessageId());
        }
    }

    @Override
    public BaseResponse sendMessageToDept(SendMsgRequest request) {
        List<Message> messageList = new ArrayList<>();
        List<User> users = userRepository.getUsersByDeptId(request.getToDeptId());
        if (CollectionUtils.isEmpty(users)) {
            return BaseResponse.error("Dept is empty!");
        }
        for (User user : users) {
            Message message = new Message();
            BeanUtils.copyProperties(request, message);
            message.setFromUser(request.getFromUserId());
            message.setToUser(user.getUserId());
            messageList.add(message);
        }
        if (messageRepository.batchInsertMessages(messageList)) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("Batch create message failed!");
        }
    }

    @Override
    public BaseResponse sendMessageToUser(SendMsgRequest request) {
        Message message = new Message();
        BeanUtils.copyProperties(request, message);
        message.setFromUser(request.getFromUserId());
        message.setToUser(request.getToUserId());
        if (messageRepository.createMessage(message)) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("Send message error");
        }
    }

    @Override
    public BaseResponse sendMessageToUsers(SendMsgRequest request) {
        List<Message> messageList = new ArrayList<>();
        for (Long toUserId : request.getToUserIds()) {
            Message message = new Message();
            BeanUtils.copyProperties(request, message);
            message.setFromUser(request.getFromUserId());
            message.setToUser(toUserId);
            messageList.add(message);
        }
        if (messageRepository.batchInsertMessages(messageList)) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("Batch create message failed!");
        }
    }
}
