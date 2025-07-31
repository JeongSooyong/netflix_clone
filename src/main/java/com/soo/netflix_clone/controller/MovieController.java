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
    public String insertMovie2(MovieVo movieVo,
        @RequestParam("moviePoster") MultipartFile moviePoster,
        RedirectAttributes redirectAttributes
    ) {
        String saveFileName = null; // 데이터베이스에 저장할 파일명

        // 파일이 선택되었는지 확인
        if (moviePoster.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "포스터 파일을 선택해주세요.");
            return "redirect:/insertMovie";
        }

        // 파일 업로드
        try {
            // 원본 파일명에서 확장자 추출
            String originalFileName = moviePoster.getOriginalFilename();
            // 확장자가 없는 파일도 처리할 수 있도록 lastIndexOf('.') 사용
            String fileExtension = "";
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            // 고유한 파일 이름 생성 (UUID + 확장자)
            saveFileName = UUID.randomUUID().toString() + fileExtension;
            
            // 파일을 저장할 디렉토리 생성
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs(); // 디렉토리가 없으면 생성
            }

            // 새로운 파일 객체 생성 (저장 경로 + 고유 파일명)
            File newFile = new File(uploadDirectory, saveFileName);
            
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = moviePoster.getInputStream(); // 업로드된 파일의 입력 스트림
                outputStream = new FileOutputStream(newFile); // 새 파일에 대한 출력 스트림
                
                byte[] buffer = new byte[1024]; // 1KB 버퍼
                int readBytes;
                // 입력 스트림에서 읽어들여 출력 스트림에 쓰기
                while ((readBytes = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readBytes);
                }
            } finally {
                // 스트림 닫기 (오류가 발생해도 반드시 닫히도록 finally 블록 사용)
                if (inputStream != null) {
                    try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
                }
                if (outputStream != null) {
                    try { outputStream.close(); } catch (IOException e) { e.printStackTrace(); }
                }
            }
            
            // MovieVo에 저장될 파일명은 이 고유한 파일 이름으로 설정
            movieVo.setMoviePoster(saveFileName);
            
        } catch (IOException e) {
            // 파일 업로드 또는 스트림 처리 중 발생한 예외
            redirectAttributes.addFlashAttribute("errorMsg", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/insertMovie";
        }

        // 3. 영화 정보 데이터베이스 저장
        try {
            // MOVIE_VIEW_COUNT 초기값 설정 (필요시 - DB DEFAULT 값 없으면)
            // movieVo.setMovieViewCount(0); 
            
            // COMMON_NO 설정 (HTML에서 hidden으로 넘기지 않았다면 여기서 설정)
            // movieVo.setCommonNo(201); 

            int result = movieService.insertMovie(movieVo); // MovieService의 insertMovie 호출
            
            if (result > 0) {
                redirectAttributes.addFlashAttribute("successMsg", "영화가 성공적으로 등록되었습니다!");
                return "redirect:/main"; // 성공 시 메인 페이지로 이동
            } else {
                // DB 저장 실패 시 업로드된 파일 삭제 (선택 사항)
                // newFile.delete(); // newFile 변수에 접근하려면 try 블록 밖에 선언해야 함
                redirectAttributes.addFlashAttribute("errorMsg", "영화 등록에 실패했습니다.");
                return "redirect:/insertMovie";
            }
        } catch (Exception e) {
            // DB 저장 로직에서 발생할 수 있는 기타 예외
            redirectAttributes.addFlashAttribute("errorMsg", "영화 등록 중 예상치 못한 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/insertMovie";
        }
    }

}
