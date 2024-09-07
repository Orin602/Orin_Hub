package com.demo.websocket;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import com.demo.domain.ChatRoom;
import com.demo.domain.MemberData;
import com.demo.service.ChatRoomService;
import com.demo.service.ChattingService;
import com.demo.persistence.MemberDataRepository;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChattingService chattingService;

    @Autowired
    private MemberDataRepository memberDataRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected to the chat room");
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);
        String[] parts = payload.split(":");

        if (parts.length < 3) {
            System.out.println("Invalid message format");
            return;
        }

        String roomIdStr = parts[0];
        String nickname = parts[1];
        String msg = parts[2];

        int roomId;
        long senderNoData;

        try {
            roomId = Integer.parseInt(roomIdStr);
            if (session.getAttributes().get("no_data") == null) {
                System.out.println("No data attribute is null");
                return;
            }
            senderNoData = (Long) session.getAttributes().get("no_data");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);

        MemberData member = memberDataRepository.findById(senderNoData)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        chattingService.saveMessage(chatRoom, member, nickname, msg);

        for (WebSocketSession wsSession : sessions) {
            if (wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage(roomId + ":" + nickname + ":" + msg));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Disconnected from the chat room");
        sessions.remove(session);

        if (session.getAttributes().get("roomId") != null) {
            int roomId = (Integer) session.getAttributes().get("roomId");
        }
    }
}
