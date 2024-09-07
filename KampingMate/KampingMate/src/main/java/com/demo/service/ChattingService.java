package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.ChatRoom;
import com.demo.domain.Chatting;
import com.demo.domain.MemberData;
import com.demo.persistence.ChatRoomRepository;
import com.demo.persistence.ChattingRepository;

@Service
public class ChattingService {

    @Autowired
    private ChattingRepository chattingRepository;
    
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public void saveMessage(ChatRoom chatRoom, MemberData member, String nickname, String content) {
        Chatting chatMessage = new Chatting();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setMember(member);
        chatMessage.setNickname(nickname);
        chatMessage.setContent(content);
        chatMessage.setWriteDate(new Date());
        chattingRepository.save(chatMessage);
    }

    public List<Chatting> getMessagesByRoom(ChatRoom chatRoom) {
        return chattingRepository.findByChatRoomOrderByWriteDateAsc(chatRoom);
    }

    public List<ChatRoom> getJoinedRooms(Long noData) {
        return chattingRepository.findJoinedChatRoomsByMemberNoData(noData);
    }

    public String getNicknameByRoomSeqAndNoData(int roomSeq, Long noData) {
        return chattingRepository.findNicknameByRoomSeqAndNoData(roomSeq, noData);
    }
    
    public void deleteMessagesByRoomAndMember(int roomSeq, Long noData) {
        chattingRepository.deleteByChatRoomRoomSeqAndMemberNoData(roomSeq, noData);
    }
    
    public void deleteChatRoom(int roomId) {
        chatRoomRepository.deleteById(roomId);
    }
}
