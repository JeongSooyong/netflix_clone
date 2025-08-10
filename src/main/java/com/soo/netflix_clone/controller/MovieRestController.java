package com.soo.netflix_clone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.vo.MovieVo;

@RestController
public class MovieRestController {

    // movie서비스 자동 주입
    @Autowired
    private MovieServiceImpl movieService;


}
