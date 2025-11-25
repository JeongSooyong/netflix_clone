package com.soo.netflix_clone.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soo.netflix_clone.service.ActorServiceImpl;

import jakarta.servlet.http.HttpSession;

import com.soo.netflix_clone.service.GenreServiceImpl;
import com.soo.netflix_clone.service.LikeServiceImpl;
import com.soo.netflix_clone.service.MovieServiceImpl;
import com.soo.netflix_clone.service.UserServiceImpl;
import com.soo.netflix_clone.service.ReviewServiceImpl;
import com.soo.netflix_clone.service.WatchHistoryServiceImpl;
import com.soo.netflix_clone.vo.ActorVo;
import com.soo.netflix_clone.vo.GenreVo;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.ReviewVo;
import com.soo.netflix_clone.vo.UserVo;
import com.soo.netflix_clone.vo.WatchHistoryVo;

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

    // like서비스 자동 주입
    @Autowired
    private LikeServiceImpl likeService;

    // review서비스 자동 주입
    @Autowired
    private ReviewServiceImpl reviewService;

    // WatchHistory서비스 자동 주입
    @Autowired
    private WatchHistoryServiceImpl watchHistoryService;

    // actor서비스 자동 주입
    @Autowired 
    private ActorServiceImpl actorService;
    
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
    @Transactional
    public String insertMovie2(
        @ModelAttribute MovieVo movieVo, 
        @RequestParam("moviePosterFile") MultipartFile posterFile,
        @RequestParam(value = "movieGenres", required = false) List<Integer> selectedGenreNos, // 장르를 List형태로 받아옴.
        @RequestParam(value = "selectedActorNos", required = false) String selectedActorNosStr,
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

                List<Integer> actorNos = null;
                if (selectedActorNosStr != null && !selectedActorNosStr.trim().isEmpty()) {
                    actorNos = Arrays.stream(selectedActorNosStr.split(","))
                                     .map(Integer::parseInt)
                                     .collect(Collectors.toList());
                    System.out.println("파싱된 actorNos: " + actorNos);
                } else {
                    System.out.println("배우가 선택되지 않았습니다.");
                }

                // actorService의 insertMovieActors 메서드를 호출
                if (actorNos != null && !actorNos.isEmpty()) {
                    int insertedActorCount = actorService.insertMovieActors(movieVo.getMovieNo(), actorNos);
                    
                    if (insertedActorCount == 0) {
                        throw new IllegalStateException("선택된 배우 정보를 영화와 연결하는 데 실패했습니다.");
                    }
                } else {
                    System.out.println("배우가 선택되지 않아 배우 연결을 건너뜁니다.");
                }
                
                redirectAttributes.addFlashAttribute("successMsg", "영화가 성공적으로 등록되었습니다!");
                
                return "redirect:/main";

            } else { // 영화 등록 실패
                redirectAttributes.addFlashAttribute("errorMsg", "영화 등록에 실패했습니다.");
                return "redirect:/movie/insertMovie"; 
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "배우 ID 형식이 올바르지 않습니다: " + e.getMessage());
            return "redirect:/movie/insertMovie"; 
        } catch (IllegalStateException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "영화 등록 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/movie/insertMovie"; 
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "영화 등록 중 예상치 못한 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/insertMovie";
        }
    }

    // 영화 상세 보기 페이지
    @GetMapping("/selectMovie/{movieNo}")
    public String selectMovie(Model model, HttpSession session, @PathVariable int movieNo) {

        // 로그인된 세션 UserVo를 가져오는 loginUser
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 조회할 MovieVo 객체를 담을 변수 vo
        MovieVo vo = new MovieVo();
        // 변수 vo에 영상 제목을 할당
        vo.setMovieNo(movieNo);

        // 서비스 계층의 selectMovie메서드 실행하여 movie라는 변수에 할당
        MovieVo movie = movieService.selectMovie(movieNo); 

        // like 서비스 계층의 countLikeMovie 메서드를 실행하여 countLikeMovie라는 변수에 할당
        int countLikeMovie = likeService.countLikeMovie(movieNo);

        // Model에 좋아요 개수 추가
        model.addAttribute("countLikeMovie", countLikeMovie);

        List<ReviewVo> review = reviewService.selectMovieReview(movieNo); 
        model.addAttribute("review", review); 

        // 영상 시청 기록을 담기 위한 객체 watchHistoryVo
        WatchHistoryVo watchHistoryVo = new WatchHistoryVo();
        // 사용자 번호, 영상 번호, 현재 날짜를 watchHistoryVo에 담는다.
        watchHistoryVo.setUserNo(loginUser.getUserNo()); 
        watchHistoryVo.setMovieNo(movieNo);
        watchHistoryVo.setWatchHistoryDate(LocalDate.now());

        // 서비스 로직의 insertWatchHistory 메서드 실행
        watchHistoryService.insertWatchHistory(watchHistoryVo);

        // 영화에 출연한 배우를 뷰에 띄우기 위한 서비스의 메서드 호출
        List<ActorVo> actors = actorService.selectActorsByMovie(movieNo);
        model.addAttribute("actors", actors);

        // 추천 여부의 기본 값(isLiked)을 false로 할당
        boolean isLiked = false; 
        // loginUser가 null이 아닌지 확인(로그인 여부 확인)
        if (loginUser != null) { 
            // 서비스계층의 isLikedMovie메서드를 동작시켜서 0보다 크다면 true를 할당
            isLiked = likeService.isLikedMovie(loginUser.getUserNo(), movieNo) > 0;
        }
        model.addAttribute("isLiked", isLiked);
        

        // movie가 null일때(영상이 선택되지 않았을때)
        if (movie == null) {
            model.addAttribute("errorMsg", "해당 영화 정보를 찾을 수 없습니다.");
            return "redirect:/main";
        }

        model.addAttribute("movie", movie);

        return "selectMovie";
    }

    // 영상 정보 수정 페이지
    @GetMapping("/updateMovie/{movieNo}")
    public String updateMovie(@PathVariable int movieNo, HttpSession session, Model model) {

        // 로그인 유저의 세션
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 서비스 계층의 selectMovie메서드를 movieVo에 담아 뷰에 영화 정보를 띄운다.
        MovieVo movieVo = movieService.selectMovie(movieNo);
        model.addAttribute("movieVo", movieVo); 

        // 영상 정보 수정 페이지의 장르 수정에 모든 장르를 List에 담아서 뷰에 띄운다.
        List<GenreVo> genreVo = genreService.selectAllGenres();
        model.addAttribute("genreVo", genreVo);

        return "updateMovie";
    }

    // 영화 정보 수정
    @PostMapping("/updateMovie2")
    public String updateMovie2(
            // MovieVo를 객체로 받아온다.
            @ModelAttribute MovieVo movieVo, 
            // updateMovie의 updateMovie2 폼의 moviePosterFile, currentMoviePoster를 받아온다.
            @RequestParam(value = "moviePosterFile", required = false) MultipartFile moviePosterFile,
            @RequestParam(value = "currentMoviePoster", required = false) String currentMoviePoster,
            RedirectAttributes redirectAttributes) {

        // updateMovie.html의 현재 DB에 저장되있는 영화포스터(currentMoviePoster)를 변수 finalMoviePosterName에 할당
        String finalMoviePosterName = currentMoviePoster;

        // moviePosterFile이 null이 아닐 경우
        if (moviePosterFile != null && !moviePosterFile.isEmpty()) {
            try {
                // 영화포스터가 null이 아닐 경우
                if (currentMoviePoster != null && !currentMoviePoster.isEmpty()) {
                     // File 객체 생성하고 파일 존재여부를 확인한 뒤 해당 파일을 삭제
                    File oldFile = new File(uploadDir, currentMoviePoster);
                    if (oldFile.exists()) { 
                        oldFile.delete(); 
                    }
                }

                // moviePosterFile의 파일명을 변수 originalFileName에 할당
                String originalFileName = moviePosterFile.getOriginalFilename();
                // 파일 확장자명을 저장할 변수 fileExtension
                String fileExtension = "";
                // 파일명(originalFileNam)이 null이 아니고 확장자명을 포함하고 있다면 
                if (originalFileName != null && originalFileName.contains(".")) {
                    // 파일 확장자명을 추출하여 fileExtension에 할당
                    fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                }

                // finalMoviePosterName에 서버에 저장할 파일명 + 확장자명 할당
                finalMoviePosterName = UUID.randomUUID().toString() + fileExtension;

                // 파일 업로드 경로 객체 생성.
                File uploadDirFile = new File(uploadDir);
                // 업로드 경로 존재여부를 확인하고 없다면 경로 생성
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                // 최종 파일 저장 경로 객체 생성하고 moviePosterFile를 destFile로 전송
                File destFile = new File(uploadDir, finalMoviePosterName);
                moviePosterFile.transferTo(destFile);

            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "포스터 파일 저장 중 오류가 발생했습니다. 다시 시도해주세요.");
                return "redirect:/updateMovie/" + movieVo.getMovieNo();
            }
        }

        // moviePoster에 finalMoviePosterName할당
        movieVo.setMoviePoster(finalMoviePosterName);

        // 서비스 계층의 updateMovie메서드의 결과를 updateResult에 할당
        int updateResult = movieService.updateMovie(movieVo);

        // updateResult가 0보다 클 경우(updateMovie가 정상적으로 실행 완료 됐을 경우)
        if (updateResult > 0) {
            redirectAttributes.addFlashAttribute("message", "영화 정보가 성공적으로 수정되었습니다.");
            return "redirect:/main";
        } else { // 영상 정보 수정이 안됐을 경우
            redirectAttributes.addFlashAttribute("errorMessage", "영화 정보 수정에 실패했습니다. (데이터베이스 업데이트 오류)");
            return "redirect:/updateMovie/" + movieVo.getMovieNo();
        }
    }

    // 영상 비공개 처리
    @PostMapping("/moviePrivate")
    public String moviePrivate(@RequestParam("movieTitle") String movieTitle, 
        HttpSession session, RedirectAttributes redirectAttributes) {

        // 현재 세션을 변수 loginUser에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        
        // loginUser가 null이거나 loginUser가 104가 아닐경우(로그인이 안된상태이거나 관리자가 아닐경우)
        if (loginUser == null || loginUser.getCommonNo() != 104) {
            redirectAttributes.addFlashAttribute("errorMessage", "관리자만 접근할 수 있는 기능입니다.");
            return "redirect:/main"; 
        }

        // 서비스계층의 moviePrivate를 호출하여 변수 updateRows에 할당
        int updatedRows = movieService.moviePrivate(movieTitle); 

        // updateRows가 0보다 크다면 영화 비공개처리 완료
        if (updatedRows > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "'" + movieTitle + "' 영화가 성공적으로 비공개 처리되었습니다.");
        } else { // 그렇지 않다면 비공개처리 실패
            redirectAttributes.addFlashAttribute("errorMessage", "'" + movieTitle + "' 영화 비공개 처리에 실패했거나 해당 영화를 찾을 수 없습니다.");
        }
        return "redirect:/main";
    }

    // 영상 검색
    @GetMapping("/searchMovie")
    public String searchMovies(
            // 뷰의 keyword를 파라미터로
            @RequestParam(value = "keyword", required = false) String keyword,
            // 검색 항목을 movie로
        @RequestParam(value = "searchCategory", defaultValue = "movie") String searchCategory,
            Model model) {

        // MovieVo를 List형태로 searchResults에 할당
        List<MovieVo> searchResults;

        

        // 검색어가 비어있지 않은 경우
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 서비스계층의 keyword를 파라미터로 하는 영상검색 메서드 호출
            searchResults = movieService.searchMovie(keyword);
            model.addAttribute("keyword", keyword);
            
            // searchResults가 null일 경우
            if (searchResults.isEmpty()) {
                model.addAttribute("searchMessage", "'" + keyword + "' 에 대한 검색 결과가 없습니다.");
            } else { // 반대의 경우
                model.addAttribute("searchMessage", "'" + keyword + "' 에 대한 검색 결과입니다.");
            }
        } else {
            return "redirect:/main";
        }
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("movies", searchResults); 

        return "searchMovie"; 
    }



}
