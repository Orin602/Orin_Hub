package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.ChatRoom;
import com.demo.domain.Chatting;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {

	List<Chatting> findByChatRoomOrderByWriteDateAsc(ChatRoom chatRoom);

    @Query("SELECT c FROM Chatting c WHERE c.member.no_data = :no_data")
    List<Chatting> findByMemberNoData(@Param("no_data") Long noData);

    @Query("SELECT DISTINCT c.chatRoom FROM Chatting c WHERE c.member.no_data = :no_data")
    List<ChatRoom> findJoinedChatRoomsByMemberNoData(@Param("no_data") Long noData);

    @Query(value = "SELECT c.nickname FROM Chatting c WHERE c.room_seq = :room_seq AND c.no_data = :no_data AND ROWNUM = 1", nativeQuery = true)
    String findNicknameByRoomSeqAndNoData(@Param("room_seq") int roomSeq, @Param("no_data") Long noData);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM chatting WHERE room_seq = :roomSeq AND no_data = :noData", nativeQuery = true)
    void deleteByChatRoomRoomSeqAndMemberNoData(@Param("roomSeq") int roomSeq, @Param("noData") Long noData);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM chatting WHERE room_seq = :roomSeq", nativeQuery = true)
    void deleteByChatRoomRoomSeq(@Param("roomSeq") int roomSeq);
    

}
