package com.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.ChatRoom;
import com.demo.persistence.ChatRoomRepository;
import com.demo.persistence.ChattingRepository;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChattingRepository chattingRepository;

    public ChatRoom createChatRoom(String title) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle(title);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getChatRoom(int roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + roomId));
    }

    public List<ChatRoom> getAvailableChatRooms(Long memberNoData) {
        List<ChatRoom> allRooms = getAllChatRooms();
        List<ChatRoom> joinedRooms = chattingRepository.findJoinedChatRoomsByMemberNoData(memberNoData);
        return allRooms.stream()
                .filter(room -> !joinedRooms.contains(room))
                .collect(Collectors.toList());
    }
    
    public void deleteChatRoom(int roomId) {
        // 먼저 chatting 테이블에서 room_seq가 일치하는 모든 레코드를 삭제합니다.
        chattingRepository.deleteByChatRoomRoomSeq(roomId);
        // 그런 다음 chatRoom을 삭제합니다.
        chatRoomRepository.deleteById(roomId);
    }
}
