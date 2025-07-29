package com.soo.netflix_clone.controller;

import java.util.List;

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
public class MainController {
    
    // user서비스 자동 주입
    @Autowired
    private UserServiceImpl userService;

    // movie서비스 자동 주입
    @Autowired
    private MovieServiceImpl movieService;


    // 로그인 했을시 메인 페이지
    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {
        // movieService의 selectAllMovies메서드를 List에 담는다.
        List<MovieVo> movies = movieService.selectAllMovies();
        model.addAttribute("movies", movies);
        return "main";  
    }
    
}