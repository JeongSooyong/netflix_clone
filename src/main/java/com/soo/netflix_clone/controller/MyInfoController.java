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
            // 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
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

    @GetMapping("/find")
    public String find() {
        return "find";
    }

    @PostMapping("/findId")
    public String findId(Model model, String userEmail) {

        UserVo vo = new UserVo();
        vo.setUserEmail(userEmail);

        UserVo foundUser = userService.findId(vo); // 서비스 호출 (이제 파라미터/반환 타입 일치)

        // 3. 반환받은 UserVo 객체에서 실제 아이디(userId)를 추출하여 모델에 담기
        if (foundUser != null && foundUser.getUserId() != null) { 
            // UserVo가 null이 아니고, 그 안의 userId 필드도 null이 아닐 때만 유효한 아이디로 간주
            model.addAttribute("foundUserId", foundUser.getUserId()); // foundUser.getUserId()로 String을 추출
        } else {
            // 아이디를 찾지 못했거나, UserVo는 받았지만 userId 필드가 비어있는 경우
            model.addAttribute("message", "해당 이메일로 등록된 아이디를 찾을 수 없습니다.");
        }


        return "findId"; // findId.html 뷰 반환
    }
    
}