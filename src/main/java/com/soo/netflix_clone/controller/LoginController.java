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

    // 서비스 자동 주입
    @Autowired
    private IUserService userService;

    // 로그인 페이지를 띄우기 위한 get
    @GetMapping({"/", "/login"}) 
    public String loginForm(Model model) {
        return "login"; 
    }

    // 로그인 페이지 폼에 입력한 값으로 로그인 처리를 하는 post
    @PostMapping("/login")
    public String login(UserVo vo, Model model, HttpSession session) {

        // 아이디 존재 여부 확인
        boolean userIdExists = userService.countUserId(vo.getUserId());

        // 서비스계층의 selectUser 메서드를 호출하여 vo 객체의 사용자 정보를 조회
        UserVo loginUser = userService.selectUser(vo);

        // 아이디가 존재하지 않는 경우
        if (!userIdExists) { 
            model.addAttribute("errorMsg", "존재하지 않는 아이디입니다.");
            return "login";
        }

        // userService의 permittedUser호출하여 UserVo타입의 permittedUser라는 변수에 할당
        UserVo permittedUser = userService.permittedUser(vo);

        // 제한된 사용자일 경우 errorMsg로 특정 메세지 전달하고 login으로 리턴
        if (permittedUser != null) { 
            model.addAttribute("errorMsg", "접근이 제한된 계정입니다.");
            return "login";
        }
        
        // 로그인 성공시
        if (loginUser != null) {
            session.setAttribute("loginUser", loginUser);
            return "redirect:/main";  // 로그인 성공 시 홈 페이지로 이동
        } else { // 로그인 실패시
            model.addAttribute("errorMsg", "비밀번호가 올바르지 않습니다.");
            return "login";  // 로그인 페이지로 다시 이동
        }
    }


    // 로그아웃 처리
    @GetMapping("/logout") 
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 종료(무효화)
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }
    

} 
