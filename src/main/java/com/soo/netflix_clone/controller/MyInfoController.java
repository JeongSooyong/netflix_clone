package com.soo.netflix_clone.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String findId2(Model model, String userEmail) {

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

    @GetMapping("/updatePw")
    public String updatePw() {
        return "updatePw";
    }

    
    @PostMapping("/updatePw2")
    public String updatePw(
        Model model,
        @RequestParam("userId") String userId, // 사용자가 폼에 입력한 아이디
        @RequestParam("userEmail") String userEmail, // 사용자가 폼에 입력한 이메일 (userId와 함께 사용자를 특정)
        @RequestParam("newPassword") String newPassword, // 새로 설정할 비밀번호 (평문)
        @RequestParam("confirmPassword") String confirmPassword // 새 비밀번호 확인 (평문)
    ) {
        // 1. 새 비밀번호와 확인 비밀번호 일치 여부 검증
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMsg", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "findPw"; 
        }

        UserVo userVo = new UserVo();
        userVo.setUserId(userId);
        userVo.setUserEmail(userEmail);
        userVo.setUserPw(newPassword); // 서비스 계층에서 이 평문 비밀번호를 해싱
        
        int updateResult = userService.updatePw(userVo); 

        if (updateResult > 0) {
            model.addAttribute("successMsg", "비밀번호가 성공적으로 변경되었습니다. 새로운 비밀번호로 로그인해주세요.");
        } else {
            model.addAttribute("errorMsg", "비밀번호 변경에 실패했습니다. 아이디 또는 이메일을 정확히 확인해주세요.");
        }
        
        return "updatePwResult";
    }

    @GetMapping("/updateMyinfo")
    public String updateMyinfo(HttpSession session, Model model) {
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("user", loginUser);
        return "updateMyinfo";
    }

}

// 비밀번호 해시 완료 , 비밀번호 재설정시 이메일 불일치시 재설정 안되도록 하기(매퍼부터 수정)