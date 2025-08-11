package com.soo.netflix_clone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.soo.netflix_clone.service.LikeServiceImpl;
import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.LikeVo;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.UserVo;

import jakarta.servlet.http.HttpSession;

@RestController
public class MovieRestController {

    // movie서비스 자동 주입
    @Autowired
    private MovieServiceImpl movieService;

    // user서비스 자동 주입
    @Autowired
    private UserServiceImpl userService;

    // like 서비스 자동 주입
    @Autowired
    private LikeServiceImpl likeService;

    @PostMapping("/likeMovie")
    @ResponseBody // 화면의 일부만 바꾸기 위해 필요한 정보만 클라이언트로 전송하기 위한 어노테이션
    public ResponseEntity<Map<String, Object>> likeMovie(@RequestBody Map<String, Integer> requestBody, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Integer movieNo = requestBody.get("movieNo");
        if (movieNo == null) {
            response.put("success", false);
            response.put("message", "영화 번호가 필요합니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // LikeVo 객체 생성 및 값 설정
            LikeVo likeVo = new LikeVo();
            likeVo.setUserNo(loginUser.getUserNo()); // LikeVo에 setUserNo 메서드가 있어야 함
            likeVo.setMovieNo(movieNo); // LikeVo에 setMovieNo 메서드가 있어야 함

            // likeMovie 메서드 호출 (LikeVo 객체 전달)
            int result = likeService.likeMovie(likeVo); // 반환 타입이 int이므로 int로 받음

            if (result > 0) { // int 반환값이 0보다 크면 성공 (예: 영향 받은 행의 수)
                response.put("success", true);
                response.put("message", "영화 추천 완료!");
            } else {
                response.put("success", false);
                response.put("message", "이미 추천했거나 처리 중 오류가 발생했습니다.");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "추천 처리 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
