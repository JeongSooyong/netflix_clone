package com.soo.netflix_clone.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.UserVo;


@Controller
public class MyInfoController {
    
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/myinfo")
    public String myInfo(HttpSession session, Model model) {
        // 세션에서 로그인된 사용자 정보 꺼내기
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/";
        }
        // 모델에 user라는 이름으로 담아서 myinfo.html로 전달
        model.addAttribute("user", loginUser);
        return "myinfo";
    }

    @PostMapping("/delUser")
    public String delUser(HttpSession session, String userId) {
        userService.delUser(userId);
        // 세션 무효화 (사용자 로그아웃)
        session.invalidate(); 
        return "redirect:/main";
    }

    @GetMapping("/findId")
    public String findId() {
        return "findId";
    }

    @PostMapping("/findId2")
    public String findId(Model model, String userEmail) {

        UserVo vo = new UserVo();
        vo.setUserEmail(userEmail);

        UserVo foundUser = userService.findId(vo); 

        if (foundUser != null && foundUser.getUserId() != null) { 
            model.addAttribute("foundUserId", foundUser.getUserId()); 
        } else {
            model.addAttribute("message", "해당 이메일로 등록된 아이디를 찾을 수 없습니다.");
        }


        return "findId2";
    }

    @PostMapping("/findPw")
    public String findPw(Model model, String userEmail, String userId) {

        UserVo vo = new UserVo();
        vo.setUserEmail(userEmail);
        vo.setUserId(userId);

        UserVo foundUserPw = userService.findPw(vo); 
        if (foundUserPw != null && foundUserPw.getUserId() != null) { 
            model.addAttribute("foundUserPw", foundUserPw.getUserPw());
        } else {
            model.addAttribute("message", "해당 이메일로 등록된 아이디를 찾을 수 없습니다.");
        }

        return "findPw";
    }
    
}