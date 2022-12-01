package com.moa.moabackend.chat.controller;

import com.moa.moabackend.chat.entity.ChatRoom;
import com.moa.moabackend.chat.entity.SocketMessage;
import com.moa.moabackend.chat.entity.SocketPlan;
import com.moa.moabackend.chat.entity.Status;
import com.moa.moabackend.chat.service.ChatRoomService;
import com.moa.moabackend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {
    private final ChatRoomService chatRoomService;
    private final JwtUtil jwtUtil;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message") // /app/socketMessage 로 받으면
//    @SendTo("/topic/socketMessage") // return 값을 /topic/socketMessage 로 넘겨준다.
    public void receiveMessage(@Payload SocketMessage socketMessage) {
        Long chatRoomId = socketMessage.getChatRoomId();
        // token 으로 userId 추출  -----> userId 로 닉네임 추출
        String userId = jwtUtil.getUserIdFromToken(socketMessage.getToken());
        System.out.println("userId : " + userId);
        // /topic/chatRoomId/message
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/message", socketMessage);
    }

    @MessageMapping("/plan")
    public void receivePlan(@Payload SocketPlan socketPlan) {
        Long chatRoomId = socketPlan.getChatRoomId();
        // /topic/chatRoomId/plan
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/plan", socketPlan);
    }

    @MessageMapping("/user")
//    public ChatRoom receiveUser(@Payload SocketMessage socketMessage, java.security.Principal principal)
    public void receiveUser(@Payload SocketMessage socketMessage) {
        Long chatRoomId = socketMessage.getChatRoomId();
        ChatRoom chatRoom = chatRoomService.setUser(chatRoomId, socketMessage);
        // /topic/chatRoomId/user
        simpMessageSendingOperations.convertAndSend("/topic/" + chatRoomId + "/user", chatRoom.getUsers());
    }
}

//    @SubscribeMapping

//    @MessageMapping("/private-message")
//    public SocketMessage receivePrivateMessage(@Payload SocketMessage message){
//        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message); // /user/David/private
//        return message;
//    }

//        messagingTemplate.convertAndSend("/topic/" + message.getRoomid(), message.getMsg());
////		messagingTemplate.convertAndSendToUser(message.getId(), "/topic/" + message.getRoomid(), message.getMsg());
//
//        return message;
//    }