package com.soo.netflix_clone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

import com.soo.netflix_clone.service.IUserService;
import com.soo.netflix_clone.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class LoginController {

    @Autowired
    private IUserService userService;

    @GetMapping({"/", "/login"}) 
    public String loginForm(Model model) {
        return "login"; 
    }


    @PostMapping("/login")
    public String login(UserVo vo, Model model, HttpSession session) {

        // 아이디 존재 여부 확인
        boolean userIdExists = userService.countUserId(vo.getUserId());

        UserVo loginUser = userService.selectUser(vo);

        // 아이디가 존재하지 않는 경우
        if (!userIdExists) { 
            model.addAttribute("errorMsg", "존재하지 않는 아이디입니다.");
            return "login";
        }


        // 제한된 사용자일 경우
        UserVo permittedUser = userService.permittedUser(vo);

        if (permittedUser != null) { 
            model.addAttribute("errorMsg", "접근이 제한된 계정입니다.");
            return "login";
        }
        
        if (loginUser != null) {
            session.setAttribute("loginUser", loginUser);
            return "redirect:/main";  // 로그인 성공 시 홈 페이지로 이동
        } else {
            model.addAttribute("errorMsg", "비밀번호가 올바르지 않습니다.");
            return "login";  // 로그인 페이지로 다시 이동
        }
    }


    // 로그아웃 처리
    @GetMapping("/logout") 
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }
    

} 
