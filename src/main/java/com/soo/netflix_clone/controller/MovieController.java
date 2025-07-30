package com.soo.netflix_clone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.soo.netflix_clone.service.GenreServiceImpl;
import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.GenreVo;

@Controller
public class MovieController {

    // user서비스 자동 주입
    @Autowired
    private UserServiceImpl userService;

    // movie서비스 자동 주입
    @Autowired
    private MovieServiceImpl movieService;

    // genre서비스 자동 주입
    @Autowired
    private GenreServiceImpl genreService;

    @GetMapping("/insertMovie")
    public String insertMovie(Model model) {

        // insertMovie 뷰에서 장르 전체를 보여주기 위한 List
        List<GenreVo> genres = genreService.selectAllGenres();
        model.addAttribute("genres", genres);

        return "insertMovie";
    }

}
