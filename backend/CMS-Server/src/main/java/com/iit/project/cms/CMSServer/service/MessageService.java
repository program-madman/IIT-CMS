package com.iit.project.cms.CMSServer.service;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.dao.DepartmentRepository;
import com.iit.project.cms.CMSServer.dao.MessageReadStatusRepository;
import com.iit.project.cms.CMSServer.dao.MessageRepository;
import com.iit.project.cms.CMSServer.dao.UserRepository;
import com.iit.project.cms.CMSServer.dto.SendMsgRequest;
import com.iit.project.cms.CMSServer.entity.Message;
import com.iit.project.cms.CMSServer.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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
        return BaseResponse.success(messageRepository.getAllMessages());
    }

    @Override
    public BaseResponse getAllMessagesSendByMe(Long uid) {
        return BaseResponse.success(messageRepository.getMessagesSentByUser(uid));
    }

    @Override
    public BaseResponse getAllMessagesSendToMe(Long uid) {
        return BaseResponse.success(messageRepository.getMessagesSentToUser(uid));
    }

    @Override
    public BaseResponse sendMessageToDept(SendMsgRequest request) {
        List<Message> messageList = new ArrayList<>();
        List<User> users = userRepository.getUsersByDeptId(request.getToDeptId());
        for (User user : users) {
            Message message = new Message();
            BeanUtils.copyProperties(request, message);
            message.setFromUser(request.getFromUserId());
            message.setToUser(user.getUserId());
            messageList.add(message);
        }
        int[] rets = messageRepository.batchInsertMessages(messageList);
        if (rets.length > 0) {
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
        int[] rets = messageRepository.batchInsertMessages(messageList);
        if (rets.length > 0) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("Batch create message failed!");
        }
    }
}
