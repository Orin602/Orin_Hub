package com.demo.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.domain.ChatRoom;
import com.demo.domain.Chatting;
import com.demo.domain.MemberData;
import com.demo.dto.DeleteRoomRequest;
import com.demo.dto.LeaveRoomRequest;
import com.demo.service.ChatRoomService;
import com.demo.service.ChattingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChattingService chattingService;

    @GetMapping("/chatService")
    public String viewChatRooms(Model model, HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }
        Long noData = loginUser.getNo_data();

        List<ChatRoom> availableRooms = chatRoomService.getAvailableChatRooms(noData);
        List<ChatRoom> joinedRooms = chattingService.getJoinedRooms(noData);
        
        model.addAttribute("chatRooms", availableRooms);
        model.addAttribute("joinedRooms", joinedRooms);
        model.addAttribute("usercode",loginUser.getUsercode());
        
        return "chat/chat-rooms";
    }

    @PostMapping("/createRoom")
    public String createChatRoom(@RequestParam String title, HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }
        chatRoomService.createChatRoom(title);
        return "redirect:/chatService";
    }

    @GetMapping("/chat/{roomId}")
    public String getChatPage(@PathVariable int roomId, @RequestParam(required = false) String nickname, Model model, HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        if (loginUser == null) {
            model.addAttribute("error", "로그인 후 이용가능합니다");
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }

        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = chattingService.getNicknameByRoomSeqAndNoData(roomId, loginUser.getNo_data());
            if (nickname == null || nickname.trim().isEmpty()) {
                model.addAttribute("error", "nickname을 입력하세요");
                return "redirect:/chatService";
            }
        }

        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);

        session.setAttribute("no_data", loginUser.getNo_data());
        session.setAttribute("roomId", roomId);

        List<Chatting> messages = chattingService.getMessagesByRoom(chatRoom);
        model.addAttribute("roomId", roomId);
        model.addAttribute("roomTitle", chatRoom.getTitle());
        model.addAttribute("username", nickname);
        model.addAttribute("messages", messages);
        model.addAttribute("userId", loginUser.getNo_data());

        return "chat/chat";
    }
    
    @PostMapping("/leaveRoom")
    public ResponseEntity<Void> leaveRoom(@RequestBody LeaveRoomRequest request, HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        chattingService.deleteMessagesByRoomAndMember(request.getRoomId(), loginUser.getNo_data());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/deleteRoom")
    public ResponseEntity<Void> deleteRoom(@RequestBody DeleteRoomRequest request, HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getUsercode() != 1) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        chatRoomService.deleteChatRoom(request.getRoomId());
        return ResponseEntity.ok().build();
    }
}
