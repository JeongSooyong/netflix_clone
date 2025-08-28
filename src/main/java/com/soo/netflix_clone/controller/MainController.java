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
import com.soo.netflix_clone.service.WatchHistoryServiceImpl;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.UserVo;
import com.soo.netflix_clone.vo.WatchHistoryVo;


@Controller
public class MainController {
    
    // user서비스 자동 주입
    @Autowired
    private UserServiceImpl userService;

    // movie서비스 자동 주입
    @Autowired
    private MovieServiceImpl movieService;

    // WatchHistory서비스 자동 주입
    @Autowired
    private WatchHistoryServiceImpl watchHistoryService;


    // 로그인 했을시 메인 페이지
    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {

        // 로그인된 사용자 정보를 담는 변수 loginUser
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 가장 최근에 시청한 영상
        // 가장 최근에 시청한 영상 (로그인된 사용자일 경우에만)
        WatchHistoryVo latestWatchedHistory = null;
        MovieVo latestWatchedMovie = null; // 실제 화면에 보여줄 MovieVo 객체

        // 로그인 여부 확인
        if (loginUser != null) { 
            // 로그인된 사용자의 userNo를 사용하여 가장 최근 시청 기록 조회 메서드 호출
            latestWatchedHistory = watchHistoryService.selectLatestWatchHistory(loginUser.getUserNo());

            // 시청 기록이 존재한다면 해당 영화 정보 조회
            if (latestWatchedHistory != null) {
                latestWatchedMovie = movieService.selectMovie(latestWatchedHistory.getMovieNo());
            }
        }
        // 모델에 가장 최근 시청 영화 정보 추가 (없으면 null이 전달됨)
        model.addAttribute("latestWatchedMovie", latestWatchedMovie);

        // movieService의 selectAllMovies메서드를 List에 담는다.
        List<MovieVo> movies = movieService.selectAllMovies();
        model.addAttribute("movies", movies);
        return "main";  
    }
    
}