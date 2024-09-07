package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.GoCamping;
import com.demo.domain.MemberData;
import com.demo.domain.Review;
import com.demo.persistence.SearchHistoryRepository;
import com.demo.service.MemberService;
import com.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

    private static final Logger logger = LoggerFactory.getLogger(MypageController.class);

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SearchHistoryRepository searchHistoryRepo;
 // 마이페이지 메인화면
    @GetMapping("/mypage")
    public String mypageView(HttpSession session, Model model) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        return "mypage/mypageMain";
    }


    @PostMapping(value = "/uploadProfileImage", consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadProfileImage(HttpSession session, @RequestParam("profileImage") MultipartFile profileImage) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (profileImage.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        String fileName = profileImage.getOriginalFilename();
        String uploadPath = "E:/student/KampingMate-3/uploads"; // 실제 저장할 경로로 수정
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        try {
            String filePath = uploadPath + "/" + fileName;
            File dest = new File(filePath);
            profileImage.transferTo(dest);
            String imagePath = "/uploads/" + fileName;

            // Save the image path in the session
            session.setAttribute("profileImagePath", imagePath);

            // 파일 업로드 성공 시 JSON 응답 반환
            Map<String, Object> response = new HashMap<>();
            response.put("imagePath", imagePath);
            response.put("message", "프로필 이미지가 성공적으로 업로드되었습니다.");

            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    
    // 내 정보 화면
    @GetMapping("/infoView")
    public String myInfoView(HttpSession session, Model model) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        
        if (loginUser == null) {
            return "redirect:/login";
        } else {
            // 최신 사용자 정보를 데이터베이스에서 가져오기
            MemberData userInfo = memberService.getMember(loginUser.getId());

            // 정보를 Thymeleaf 템플릿에 전달
            model.addAttribute("name", userInfo.getName());
            model.addAttribute("id", userInfo.getId());
            model.addAttribute("password", "********");
            model.addAttribute("email", userInfo.getEmail());
        }
        return "mypage/infoView";
    }

    @GetMapping("/Outdoor")
    public String outdoor(HttpSession session, Model model) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "mypage/Outdoor";
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");

        if (loginUser != null) {
            try {
                memberService.withdrawMember(loginUser.getId());
                session.invalidate(); // 세션 무효화
                return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
            } catch (Exception e) {
                logger.error("Error during withdrawal", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 탈퇴 중 오류가 발생했습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다. 다시 로그인해주세요.");
        }
    }

    @GetMapping("/guestBook")
    public String myReviewsView(HttpSession session, Model model) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        } else {
            // 사용자가 작성한 리뷰 가져오기
            List<Review> reviews = reviewService.getReviewsById(loginUser.getId());

            // 리뷰가 존재하는지 확인하고 모델에 추가
            if (reviews != null && !reviews.isEmpty()) {
                model.addAttribute("myReviews", reviews); // 수정된 부분: "myReviews"로 변경
            }
        }
        return "mypage/guestBook";
    }

    @GetMapping("/review/{reviewId}")
    public String showReviewDetail(@PathVariable("reviewId") int reviewId, Model model) {
        // 리뷰 ID를 사용하여 해당 리뷰의 세부 정보 가져오기
        Review review = reviewService.getReviewById(reviewId);

        // 리뷰가 존재하는지 확인하고 모델에 추가
        if (review != null) {
            model.addAttribute("review", review);
        }

        return "mypage/reviewDetail";
    }

    @GetMapping("/bookMark")
    public String myBookmarksView(HttpSession session, Model model) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        } else {
            // 사용자가 북마크한 리뷰 가져오기
            List<Review> bookmarkedReviews = reviewService.getBookmarkedReviews(loginUser.getId());

            // 북마크한 리뷰가 존재하는지 확인하고 모델에 추가
            if (bookmarkedReviews != null && !bookmarkedReviews.isEmpty()) {
                model.addAttribute("bookmarkedReviews", bookmarkedReviews);
            }
        }
        return "mypage/bookMark";
    }


    @GetMapping("/myRecommend")
    public String myRecommend(HttpSession session, Model model) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        // Fetch search history and add to the model
        List<GoCamping> searchHistoryList = searchHistoryRepo.findAllCampingByMemberNoData(loginUser.getNo_data());
        model.addAttribute("searchHistoryList", searchHistoryList);

        // Fetch recommendations based on search history
        List<GoCamping> recommendedCampingList = searchHistoryRepo.findAllCampingByMemberNoData(loginUser.getNo_data());
        model.addAttribute("recommendedCampingList", recommendedCampingList);

        return "mypage/recommend";
    }





}
