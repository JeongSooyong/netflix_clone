package com.soo.netflix_clone.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.UserVo;


@Controller
public class MyInfoController {
    
    // user서비스 자동 주입
    @Autowired
    private UserServiceImpl userService;

    // 스프링 시큐리티에 등록한 BCryptPasswordEncoder 자동 주입
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; 

    // 내 정보 페이지
    @GetMapping("/myinfo")
    public String myInfo(HttpSession session, Model model) {

        // 세션에서 로그인된 사용자 정보 꺼내기
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // 로그인된 계정이 없을시
        if (loginUser == null) {
            return "redirect:/";
        }

        // 모델에 user라는 이름으로 담아서 myinfo로 전달
        model.addAttribute("user", loginUser);
        return "myinfo";
    }

    // 회원탈퇴
    @PostMapping("/delUser")
    public String delUser(HttpSession session, String userId) {
        // 현재 세션에서 userService의 delUser메서드 호출
        userService.delUser(userId);
        // 세션 무효화 (사용자 로그아웃) 후 login페이지로 이동
        session.invalidate(); 
        return "redirect:/login";
    }

    // 아이디 찾기 페이지
    @GetMapping("/findId")
    public String findId() {
        return "findId";
    }

    // 아이디 찾기
    @PostMapping("/findId2")
    public String findId2(Model model, String userEmail) {

        // 사용자 정보를 담을 UserVo타입의 vo 객체 생성
        UserVo vo = new UserVo();

        // vo에 userEmail을 담아준다.
        vo.setUserEmail(userEmail);

        // UserVo타입의 foundUser라는 변수에 vo객체에 담긴 정보를 바탕으로 userService의 findId 메서드 호출
        UserVo foundUser = userService.findId(vo); 

        // foundUser가 null이 아닐때
        if (foundUser != null && foundUser.getUserId() != null) { 
            model.addAttribute("foundUserId", foundUser.getUserId()); 
        } else { // foundUser가 null일때
            model.addAttribute("message", "해당 이메일로 등록된 아이디를 찾을 수 없습니다.");
        }

        return "findId2";
    }

    // 비밀번호 재설정 페이지
    @GetMapping("/updatePw")
    public String updatePw() {
        return "updatePw";
    }

    // 비밀번호 재설정
    @PostMapping("/updatePw2")
    public String updatePw(
        Model model,
        @RequestParam("userId") String userId, // 사용자가 폼에 입력한 아이디
        @RequestParam("userEmail") String userEmail, // 사용자가 폼에 입력한 이메일
        @RequestParam("newPassword") String newPassword, // 새로 설정할 비밀번호
        @RequestParam("confirmPassword") String confirmPassword // 새 비밀번호 확인
    ) {
        // 새비밀번호, 새비밀번호 재입력이 일치하지 않을때
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMsg", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "findPw"; 
        }

        // 세션의 사용자 정보를 담을 UserVo타입의 vo 객체 생성
        UserVo vo = new UserVo();
        // vo 객체에 userId, userEmail, newPassword를 담아준다.
        vo.setUserId(userId);
        vo.setUserEmail(userEmail);
        vo.setUserPw(newPassword); // 서비스 계층에서 이 평문 비밀번호를 해싱
        
        // userService의 updatePw메서드를 호출하여 결과를 updateResult에 할당
        int updateResult = userService.updatePw(vo); 

        // updateResult 변수가 0 이상일 경우(비밀번호 재설정 됐을 경우)
        if (updateResult > 0) {
            model.addAttribute("successMsg", "비밀번호가 성공적으로 변경되었습니다. 새로운 비밀번호로 로그인해주세요.");
        } else { // 비밀번호 재설정이 실패 했을 경우
            model.addAttribute("errorMsg", "비밀번호 변경에 실패했습니다. 아이디 또는 이메일을 정확히 확인해주세요.");
        }
        
        return "updatePwResult";
    }

    // 내 정보 수정 페이지
    @GetMapping("/updateMyinfo")
    public String updateMyinfo(HttpSession session, Model model) {
        // 현재 세션의 loginUser로 저장된 사용자를 가져와 UserVo타입으로 변환하여 UserVo타입의 loginUser 변수에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        // 뷰에서 사용하기 위해 loginUser를 user라는 이름으로 model에 추가
        model.addAttribute("user", loginUser);
        return "updateMyinfo";
    }

    // 내 정보 수정
    @PostMapping("/updateMyinfo2")
    public String updateMyinfo2(Model model, HttpSession session, RedirectAttributes redirectAttributes,
        @RequestParam("userId") String userId,
        @RequestParam("userEmail") String userEmail,
        @RequestParam(value = "newPassword", required = false) String newPassword,
        @RequestParam(value = "confirmPassword", required = false) String confirmPassword) {

        // 현재 세션의 loginUser로 저장된 사용자를 가져와 UserVo타입으로 변환하여 UserVo타입의 loginUser 변수에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // 새로 설정한 비밀번호를 담을 변수 finalPassword
        String finalPassword = null;

        // 새로 설정할 비밀번호가 null이 아닐 경우
        if (newPassword != null && !newPassword.isEmpty()) {

            // 새로 설정할 비밀번호가 비밀번호확인칸과 일치하지 않을 경우
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMsg", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                return "redirect:/updateMyinfo";
            }
            
            // 비밀번호를 해시화하여 변수 finalPassword에 담아준다.
            finalPassword = bCryptPasswordEncoder.encode(newPassword);
        }

        // 사용자 정보를 담을 UserVo타입의 vo 객체 생성
        UserVo vo = new UserVo();
        // vo 객체에 userId, userEmail, finalPassword를 담아준다.
        vo.setUserId(userId);
        vo.setUserEmail(userEmail);
        vo.setUserPw(finalPassword);

        // userService의 updateMyinfo를 호출하여 변수 updateResult에 할당
        int updateResult = userService.updateMyinfo(vo);

        // updateResult가 0보다 클 경우(사용자 정보 변경에 성공했을 경우)
        if (updateResult > 0) {
            // 변경된 userEmail을 담아준다.
            // 변경된 비밀번호는 뷰에서 표시하지 않기 때문에 비밀번호는 담을 필요없음.
            loginUser.setUserEmail(userEmail);
            session.setAttribute("loginUser", loginUser);

            redirectAttributes.addFlashAttribute("successMsg", "회원 정보가 성공적으로 수정되었습니다!");
            return "redirect:/myinfo";
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "회원 정보 수정에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/updateMyinfo";
        }
    }

}
