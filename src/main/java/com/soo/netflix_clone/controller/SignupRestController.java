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

    @Autowired
    private UserServiceImpl service; // UseeServiceImpl 주입

    @GetMapping("/checkUserId")
    @ResponseBody
    public Map<String, Boolean> checkUserIdDuplication(@RequestParam("userId") String userId) {
        Map<String, Boolean> response = new HashMap<>();
        // service.countUserId()가 boolean을 반환하므로, 그 값을 그대로 사용
        boolean isDuplicated = service.countUserId(userId);
        response.put("isDuplicated", isDuplicated); // isDuplicated: true (중복), false (사용 가능)
        return response;
    }
}

