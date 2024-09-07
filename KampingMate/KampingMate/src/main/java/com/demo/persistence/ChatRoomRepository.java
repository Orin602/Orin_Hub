package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}
