package com.soo.netflix_clone.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.soo.netflix_clone.service.UserServiceImpl;

@RestController
public class SignupRestController {

    // user서비스 자동 주입
    @Autowired
    private UserServiceImpl userService; 

    // 아이디 중복 체크
    @GetMapping("/checkUserId")
    @ResponseBody // 화면의 일부만 바꾸기 위해 필요한 정보만 클라이언트로 전송하기 위한 어노테이션
    public Map<String, Boolean> checkUserIdDuplication(@RequestParam("userId") String userId) {

        // 아이디 중복 여부를 담을 HashMap
        Map<String, Boolean> response = new HashMap<>();

        // userService의 countUserId메서드를 호출하여 isDuplicated에 할당
        boolean isDuplicated = userService.countUserId(userId);

        // response(HashMap)에 값을 담아준다.
        response.put("isDuplicated", isDuplicated);

        return response;
    }
}

