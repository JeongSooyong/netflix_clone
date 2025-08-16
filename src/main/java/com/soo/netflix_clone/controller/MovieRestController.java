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
import com.soo.netflix_clone.service.ReviewServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.LikeVo;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.ReviewVo;
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

    // review 서비스 자동 주입
    @Autowired
    private ReviewServiceImpl reviewService;

    // 영상 추천
    @PostMapping("/likeMovie")
    @ResponseBody // 화면의 일부만 바꾸기 위해 필요한 정보만 클라이언트로 전송하기 위한 어노테이션
    public ResponseEntity<Map<String, Object>> likeMovie(@RequestBody Map<String, Integer> requestBody,
            HttpSession session) {

        // Map형태의 response 변수 초기화
        Map<String, Object> response = new HashMap<>();

        // 세션에서 로그인 사용자 정보를 loginUser에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // loginUser가 null이라면 (현재 로그인이 안됐다면, 세션이 만료되었다면)
        if (loginUser == null) {
            // 응답 성공 여부와 메세지를 response(HashMap)에 담는다.
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            // 요청은 받았지만 인증이 되지 않았음을 반환
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // movieVo에 movieNo의 키값을 할당
        Integer movieNo = requestBody.get("movieNo");
        // movieNo가 null이라면
        if (movieNo == null) {
            // response(HashMap)에 false와 "영화 번호가 필요합니다."의 메세지를 put한다.
            response.put("success", false);
            response.put("message", "영화 번호가 필요합니다.");
            // 요청은 받았지만 HTTP 상태 코드 400임을 반환
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // LikeVo 객체 생성
            LikeVo likeVo = new LikeVo();
            // 세션에 저장된 현재 로그인 사용자(loginUser)의 userNo를 likeVo의 userNo 필드에 할당
            likeVo.setUserNo(loginUser.getUserNo()); 
            // selectMoive에서 받은 movieNo를 likeVo의 movieNo에 할당
            likeVo.setMovieNo(movieNo); 

            // 서비스계층의 likeMovie 메서드 호출하여 변수 result에 할당
            int result = likeService.likeMovie(likeVo);

            // result가 0보다 크다면(서비스 계층의 likeMovie메서드가 정상적으로 작동되었을때)
            if (result > 0) { 
                // response(HashMap)에 해당 값들을 put
                response.put("success", true);
                response.put("message", "영화 추천 완료!");

                // 현재 추천 개수를 다시 조회
                int currentCountLikeMovie = likeService.countLikeMovie(movieNo);
                // response(HashMap)에 현재 추천 개수를 다시 담는다.
                response.put("currentCountLikeMovie", currentCountLikeMovie);

            } else {
                response.put("success", false);
                response.put("message", "이미 추천했거나 처리 중 오류가 발생했습니다.");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);

        // 예외 발생시(HTTP 상태 코드가 500일때)
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "추천 처리 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 영상 추천 취소
    @PostMapping("/likeMovieCancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likeMovieCancel(@RequestBody Map<String, Integer> requestBody,
            HttpSession session) {
        
        // Map형태의 response 변수 초기화
        Map<String, Object> response = new HashMap<>();

        // 세션에서 로그인 사용자 정보를 loginUser에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // loginUser가 null이라면 (로그인이 되지않았거나, 세션이 만료되었을때)
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // movieVo에 movieNo의 키값을 할당
        Integer movieNo = requestBody.get("movieNo");
        // movieNo가 null일때(영화가 선택되지않았을때)
        if (movieNo == null) {
            response.put("success", false);
            response.put("message", "영화 번호가 필요합니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // likeVo 변수 초기화
            LikeVo likeVo = new LikeVo();
            // 세션에 저장된 현재 로그인 사용자(loginUser)의 userNo를 likeVo의 userNo 필드에 할당
            likeVo.setUserNo(loginUser.getUserNo());
            // selectMovie에서 선택된 영화의 movieNo를 movieNo를 likeVo의 movieNo에 할당
            likeVo.setMovieNo(movieNo);

            // 서비스 계층의 likeMovieCancel 메서드를 호출하여 변수 deleted에 할당
            int deleted = likeService.likeMovieCancel(likeVo); 

            // deleted가 0보다 클 때 (likeMovieCancel메서드가 정상적으로 작동되었을때)
            if (deleted > 0) {
                response.put("success", true);
                response.put("message", "추천이 취소되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "추천을 취소할 수 없습니다.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "취소 처리 중 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 리뷰 작성
    @PostMapping("/insertMovieReview")
    @ResponseBody
    public ResponseEntity<String> insertMovieReview(@RequestBody ReviewVo reviewVo){
        
        // 리뷰 서비스 계층의 insertMovieReview메서드 호출
        reviewService.insertMovieReview(reviewVo);

        return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
    }

}
