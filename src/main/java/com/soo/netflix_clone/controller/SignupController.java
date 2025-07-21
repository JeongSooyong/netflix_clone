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

    @Autowired
    private UserServiceImpl service;

    // 회원가입
    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    // html의 회원가입 버튼을 누르면 메서드가 실행된다.
    @PostMapping("/signup")
    public String signupPost(UserVo vo, Model model) {
        int result = service.insertUser(vo);
        if(result > 0) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "회원가입에 실패했습니다.");
            return "signup";
        }
    }
} 
