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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soo.netflix_clone.service.GenreServiceImpl;
import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.vo.GenreVo;
import com.soo.netflix_clone.vo.MovieVo;

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

    // application.properties의 'file.upload-dir' 값을 주입받음
    // 이 경로는 File 객체 생성 시 문자열로 사용됩니다.
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
        @RequestParam(value = "movieGenres", required = false) List<Integer> selectedGenreNos,
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
            String originalFileName = posterFile.getOriginalFilename(); 
            String fileExtension = "";
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            saveFileName = UUID.randomUUID().toString() + fileExtension;

            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            File newFile = new File(uploadDirectory, saveFileName);
            
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = posterFile.getInputStream();
                outputStream = new FileOutputStream(newFile);
                
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readBytes);
                }
            } finally {
                if (inputStream != null) {
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
    
        if (selectedGenreNos != null && !selectedGenreNos.isEmpty()) {
            movieVo.setMovieGenre(selectedGenreNos.get(0)); 
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "장르를 하나 이상 선택해주세요.");
            return "redirect:/insertMovie";
        }

        try {
            movieVo.setCommonNo(201); 

            int result = movieService.insertMovie(movieVo); 
            

            if (result > 0) {
                redirectAttributes.addFlashAttribute("successMsg", "영화가 성공적으로 등록되었습니다!");
                return "redirect:/main";
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "영화 등록에 실패했습니다.");
                return "redirect:/insertMovie";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "영화 등록 중 예상치 못한 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/insertMovie";
        }
    }

}
