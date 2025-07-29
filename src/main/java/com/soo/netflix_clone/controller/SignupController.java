package com.soo.netflix_clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.UserVo;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {

    // user서비스 자동 주입
    @Autowired
    private UserServiceImpl userService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    // 회원가입
    @PostMapping("/signup")
    public String signupPost(UserVo vo, Model model) {

        // userService의 insertUser메서드를 호출하여 변수 result에 할당
        int result = userService.insertUser(vo);

        // result가 0이상일 경우(회원가입에 성공했을 경우)
        if(result > 0) {
            return "redirect:/";
        } else { // 회원가입에 실패했을 경우
            model.addAttribute("error", "회원가입에 실패했습니다.");
            return "signup";
        }
    }
} 
