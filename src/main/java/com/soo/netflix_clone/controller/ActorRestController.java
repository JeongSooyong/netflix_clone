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

import com.soo.netflix_clone.service.ActorServiceImpl;
import com.soo.netflix_clone.service.LikeServiceImpl;
import com.soo.netflix_clone.vo.ActorVo;
import com.soo.netflix_clone.vo.LikeVo;
import com.soo.netflix_clone.vo.UserVo;

import jakarta.servlet.http.HttpSession;

@RestController
public class ActorRestController {

    // Actor 서비스 자동주입
    @Autowired
    private ActorServiceImpl actorService;

    // Like 서비스 자동주입
    @Autowired
    private LikeServiceImpl likeService;

    // 배우 검색
    @GetMapping("/api/actors/search")
    @ResponseBody
    public List<ActorVo> searchActor(@RequestParam(value = "keyword", required = false) String keyword) {
        
        // 검색 키워드가 null 일때
        if (keyword == null || keyword.trim().isEmpty()) {
            return actorService.selectAllActor();
        } 
        return actorService.searchActor(keyword);
    }

    // 배우 추천
    @PostMapping("/likeActor")
    @ResponseBody // 화면의 일부만 새로고침없이 바꾸기 위해 필요한 정보만 클라이언트 전송하기 위한 어노테이션
    public ResponseEntity<Map<String, Object>> likeActor(@RequestBody Map<String, Integer> requestBody,
            HttpSession session) {

        //Map 형태의 response 변수 초기화
        Map<String, Object> response = new HashMap<>();

        // 세션에서 로그인 사용자 정보를 변수 loginUser에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // loginUser가 null이라면 (현재 로그인된 상태가 아니라면)
        if (loginUser == null) {
            // 응답 성공 여부와 메세지를 response(HashMap)에 담는다.
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            // 요청은 받았지만 인증이 되지 않았음을 반환
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // actorVo에 actorNo의 키값을 할당
        Integer actorNo = requestBody.get("actorNo");
        // actorNo가 null이라면
        if (actorNo == null) {
            //response(HashMap)에 false와 "배우 번호가 필요합니다."라는 메세지를 put
            response.put("success", false);
            response.put("message", "배우 번호가 필요합니다.");
            // 요청은 받았지만 HTTP 상태 코드가 400임을 반환
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            //LikeVo 객체 생성
            LikeVo likeVo = new LikeVo();
            // 세션에 저장된 현재 로그인 사용자(loginUser)의 userNo를 likeVo의 userNo 필드에 할당
            likeVo.setUserNo(loginUser.getUserNo());
            // selectActor에서 받은 actorNo를 likeVo의 actorNo에 할당
            likeVo.setActorNo(actorNo);

            // 서비스계층의 likeActor 메서드 호출하여 변수 result에 할당
            int result = likeService.likeActor(likeVo);

            //result가 0보다 클때(서비스 계층의 likeActor메서드가 정상적으로 실행됐을때)
            if (result > 0) {
                // response(HashMap)에 해당 값들을 put
                response.put("success", true);
                response.put("message", "배우 추천 완료!");

                // likeActor 메서드가 정상적으로 동작된 후 추천 개수를 다시 조회
                int currentCountLikeActor = likeService.countLikeActor(actorNo);

                // response(HashMap)에 현재 추천 개수를 다시 할당
                response.put("currentCountLikeActor", currentCountLikeActor);
            } else {
                response.put("success", false);
                response.put("message", "이미 추천했거나 처리 중 오류가 발생했습니다.");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);

        // 예외 발생시(HTTP 상태 코드가 500일때)
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "추천 처리 중 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 배우 추천 취소
    @PostMapping("/likeActorCancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likeActorCancel(@RequestBody Map<String, Integer> requestBody,
            HttpSession session) {
        
        //Map 형태의 response 변수 초기화
        Map<String, Object> response = new HashMap<>();

        // 세션에서 로그인 사용자 정보를 변수 loginUser에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // loginUser가 null일때(로그인상태가 아니거나, 세션이 만료됐을때)
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // actorVo에 actorNo의 키값 할당
        Integer actorNo = requestBody.get("actorNo");
        // actorNo가 null일때(배우가 선택되지않았을경우)
        if (actorNo == null) {
            response.put("success", false);
            response.put("message", "배우가 선택되지 않았습니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // likeVo 변수 초기화
            LikeVo likeVo = new LikeVo();
            // 세션에 저장된 현재 로그인 사용자(loginUser)의 userNo 필드에 할당
            likeVo.setUserNo(loginUser.getUserNo());
            // selectActor에서 선택된 배우의 actorNo likeVo의 actorNo에 할당
            likeVo.setActorNo(actorNo);

            // 서비스 계층의 likeActorCancel 메서드를 호출하여 변수 deleted에 할당
            int deleted = likeService.likeActorCancel(likeVo);

            // deleted가 0보다 클 때 (likeActorCancel메서드가 정상적으로 호출됐을때)
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
            response.put("message", "취소 처리 중 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
