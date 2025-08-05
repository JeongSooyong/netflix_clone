package com.soo.netflix_clone.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import com.soo.netflix_clone.service.GenreServiceImpl;
import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.GenreVo;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.UserVo;

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

    // application.properties의 'file.upload-dir' 값을 주입
    // 이 경로는 File 객체 생성 시 문자열로 사용
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 영상 등록 페이지
    @GetMapping("/insertMovie")
    public String insertMovie(Model model) {

        // insertMovie 뷰에서 장르 전체를 보여주기 위한 List
        List<GenreVo> genres = genreService.selectAllGenres();
        model.addAttribute("genres", genres);

        return "insertMovie";
    }

    // 영상 등록
    @PostMapping("/insertMovie2")
    public String insertMovie2(
        @ModelAttribute MovieVo movieVo, 
        @RequestParam("moviePosterFile") MultipartFile posterFile,
        @RequestParam(value = "movieGenres", required = false) List<Integer> selectedGenreNos, // 장르를 List형태로 받아옴.
        RedirectAttributes redirectAttributes
    ) {

        // 데이터베이스에 저장할 파일명
        String saveFileName = null; 

        // 파일이 선택되었는지 확인하는 if문
        if (posterFile.isEmpty()) { 
            redirectAttributes.addFlashAttribute("errorMsg", "포스터 파일을 선택해주세요.");
            return "redirect:/insertMovie";
        }

        // 파일 업로드
        try {
            // originalFileName에 posterFile의 파일명 할당
            String originalFileName = posterFile.getOriginalFilename(); 
            // 파일의 확장자명을 담을 변수 fileExtension
            String fileExtension = "";
            // originalFileName이 null이 아니거나 파일 확장자명이 있는지 확인할 조건문
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                // 파일의 확장자명을 fileExtension에 할당
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            // 서버에 파일을 저장할 때 사용할 고유한 파일명 + 확장자명을 saveFileName에 할당
            saveFileName = UUID.randomUUID().toString() + fileExtension;

            // 파일을 저장할 폴더가 존재하는지 확인하는 코드
            // 파일을 관리하기 위한 객체 생성
            File uploadDirectory = new File(uploadDir);
            // 폴더가 존재하는지 확인하는 조건문
            if (!uploadDirectory.exists()) {
                // 업로드파일을 담을 폴더가 없다면 폴더 생성
                uploadDirectory.mkdirs();
            }

            // 업로드된 파일이 실제로 저장될 '경로 + 파일명' 정보를 가진 File 객체 생성
            File newFile = new File(uploadDirectory, saveFileName);
            
            // insertMovie 뷰에서 파일을 읽고, 폴더에 담기 위한 변수 선언
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                // 업로드된 파일에서 데이터를 읽기위한 코드
                inputStream = posterFile.getInputStream();
                // 새 파일에 데이터를 내보낼 코드
                outputStream = new FileOutputStream(newFile);
                
                // 데이터를 임시로 담을 코드
                byte[] buffer = new byte[1024];
                // 읽어들인 바이트 수를 저장할 변수 readBytes
                int readBytes;
                // 파일을 끝까지 읽을 때까지 반복
                while ((readBytes = inputStream.read(buffer)) != -1) {
                    // 읽은 만큼만 새 파일에 쓰기
                    outputStream.write(buffer, 0, readBytes);
                }
            } finally {
                // inputStream 변수가 null인지 확인
                if (inputStream != null) { 
                    // 입력 스트림이 사용하던 자원을 반환
                    try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
                }
                if (outputStream != null) {
                    try { outputStream.close(); } catch (IOException e) { e.printStackTrace(); }
                }
            }
            
            movieVo.setMoviePoster(saveFileName); 
            
        } catch (IOException e) {
            e.printStackTrace(); // 스택 트레이스 출력
            redirectAttributes.addFlashAttribute("errorMsg", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/insertMovie";
        }
    
        // List형태로 받은 장르에 값이 할당 되었는지 확인하는 조건문
        if (selectedGenreNos != null && !selectedGenreNos.isEmpty()) {
            // 장르가 선택되었다면, 리스트의 첫 번째 인덱스 장르 ID를 MovieVo 객체의 movieGenre 필드에 설정
            movieVo.setMovieGenre(selectedGenreNos.get(0)); 
        } else { // List형태로 받은 장르가 비어있을 때
             // 오류 메시지를 flash attribute에 담아, 리다이렉트 후 사용자에게 전달
            redirectAttributes.addFlashAttribute("errorMsg", "장르를 하나 이상 선택해주세요.");
            return "redirect:/insertMovie";
        }

        try {
            // movieVo클래스의 commonNo에 201을 할당.
            movieVo.setCommonNo(201); 

            // 서비스 계층의 insertMovie메서드 실행
            int result = movieService.insertMovie(movieVo); 
            
            // result가 0보다 클 경우(영화가 성공적으로 등록되었을 경우)
            if (result > 0) {
                redirectAttributes.addFlashAttribute("successMsg", "영화가 성공적으로 등록되었습니다!");
                return "redirect:/main";
            } else { // 그렇지 않을 경우(영화 등록에 실패했을경우)
                redirectAttributes.addFlashAttribute("errorMsg", "영화 등록에 실패했습니다.");
                return "redirect:/insertMovie";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "영화 등록 중 예상치 못한 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/insertMovie";
        }
    }

    // 영화 상세 보기 페이지
    @GetMapping("/selectMovie/{movieTitle}")
    public String selectMovie(@PathVariable("movieTitle") String movieTitle, Model model, HttpSession session) {

        // 로그인된 세션 UserVo를 가져오는 loginUser
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 조회할 MovieVo 객체를 담을 변수 vo
        MovieVo vo = new MovieVo();
        // 변수 vo에 영상 제목을 할당
        vo.setMovieTitle(movieTitle);

        // 서비스 계층의 selectMovie메서드 실행하여 movie라는 변수에 할당
        MovieVo movie = movieService.selectMovie(movieTitle); 

        // movie가 null일때(영상이 선택되지 않았을때)
        if (movie == null) {
            model.addAttribute("errorMsg", "해당 영화 정보를 찾을 수 없습니다.");
            return "redirect:/main";
        }

        model.addAttribute("movie", movie);

        return "selectMovie";
    }

    // 영상 정보 수정 페이지
    @GetMapping("/updateMovie/{movieTitle}")
    public String updateMovie(@PathVariable("movieTitle") String movieTitle, HttpSession session, Model model) {

        // 로그인 유저의 세션
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 서비스 계층의 selectMovie메서드를 movieVo에 담아 뷰에 영화 정보를 띄운다.
        MovieVo movieVo = movieService.selectMovie(movieTitle);
        model.addAttribute("movieVo", movieVo); 

        // 영상 정보 수정 페이지의 장르 수정에 모든 장르를 List에 담아서 뷰에 띄운다.
        List<GenreVo> genreVo = genreService.selectAllGenres();
        model.addAttribute("genreVo", genreVo);

        return "updateMovie";
    }


    @PostMapping("/updateMovie/{movieTitle}")
    public String updateMovie2() {
        return "selectMovie";
    }

}
