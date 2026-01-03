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

import com.soo.netflix_clone.service.ActorServiceImpl;
import com.soo.netflix_clone.service.LikeServiceImpl;
import com.soo.netflix_clone.service.ReviewServiceImpl;
import com.soo.netflix_clone.vo.ActorVo;
import com.soo.netflix_clone.vo.MovieVo;
import com.soo.netflix_clone.vo.ReviewVo;
import com.soo.netflix_clone.vo.UserVo;

import jakarta.servlet.http.HttpSession;

@Controller
public class ActorController {

    // Actor서비스 자동 주입
    @Autowired
    private ActorServiceImpl actorService;

    // Like서비스 자동 주입
    @Autowired
    private LikeServiceImpl likeService;

    // Review서비스 자동 주입
    @Autowired
    private ReviewServiceImpl reviewService;

    // application.properties의 'file.upload-dir' 값을 주입
    // 이 경로는 File 객체 생성 시 문자열로 사용
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 배우 조회
    @GetMapping("/actors/{actorNo}")
    public String selectActor(@PathVariable("actorNo") int actorNo, Model model, HttpSession session) {

        // 서비스의 selectActor메서드 호출
        ActorVo actor = actorService.selectActor(actorNo);
        model.addAttribute("actor", actor);

        // 로그인된 세션 UserVo를 가져오는 loginUser
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // like 서비스 계층의 countLikeActor 메서드를 호출하여 countLikeActor에 할당
        int countLikeActor = likeService.countLikeActor(actorNo);

        // review 서비스 계층의 selectActorReview 메서드 호출
        List<ReviewVo> reviews = reviewService.selectActorReview(actorNo);
        model.addAttribute("reviews", reviews);

        model.addAttribute("countLikeActor", countLikeActor);


        // 추천 여부의 기본 값을 false로 할당
        boolean isLiked = false;

        // loginUser가 null이 아닌지 확인(로그인 여부 확인)
        if (loginUser != null) {
            // 로그인된 상태라면 서비스계층의 isLikedActor 메서드를 동작시켜서 0보다 크다면 true를 할당
            isLiked = likeService.isLikedActor(loginUser.getUserNo(), actorNo) > 0;
        }
        model.addAttribute("isLiked", isLiked);

        // 배우의 출연 영화 목록 조회하는 코드
        // actorService의 selectMoviesByActorNo메서드를 호출하여 List타입의 변수 filmography에 할당
        List<MovieVo> filmography = actorService.selectMoviesByActorNo(actorNo);
        model.addAttribute("filmography", filmography);

        return "actorinfo";
    }

    // 배우 등록
    @GetMapping("/insertActor")
    public String insertActor(Model model, HttpSession session) {
        model.addAttribute("actorVo", new ActorVo());
        return "insertActor";
    }

    // 배우 등록 Post
    @PostMapping("/insertActor2")
    public String insertActor2(
            @ModelAttribute ActorVo actorVo,
            @RequestParam("actorPosterFile") MultipartFile posterFile,
            RedirectAttributes redirectAttributes) {

        // 데이터베이스에 저장할 파일명
        String saveFileName = null;

        // 파일이 선택되었는지 확인하는 if문
        if (posterFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "포스터 파일을 선택해주세요");
            return "redirect:/insertActor";
        }

        // 파일 업로드
        try {
            // originalFileName에 posterFile의 파일명 할당
            String originalFileName = posterFile.getOriginalFilename();
            // 파일의 확장자명을 담을 변수 fileExtension
            String fileExtension = "";
            // originalFileName이 null이 아니거나 파일 확장자명이 있는지 확인하는 조건문
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
                uploadDirectory.mkdir();
            }

            // 업로드된 파일이 실제로 저장될 경로 + 파일명 정보를 가진 File 객체 생성
            File newFile = new File(uploadDirectory, saveFileName);

            // insertActor 뷰에서 파일을 읽고 폴더에 담을 변수 선언
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                // 업로드된 파일에서 데이터를 읽기위한 코드
                inputStream = posterFile.getInputStream();
                // 새 파일에 데이터를 내보낼 코드
                outputStream = new FileOutputStream(newFile);

                // 데이터를 임시로 담을 코드
                byte[] buffer = new byte[1024];
                // 읽어들인 바이트 수를 저장할 변수 readBytes;
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
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            actorVo.setActorPoster(saveFileName);
            actorService.insertActor(actorVo);

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/insertActor";
        }

        return "redirect:/main";
    }

    // insertMovie에서 배우 선택 버튼을 클릭했을때 나오는 팝업창
    @GetMapping("/selectAllActor")
    public String selectAllActor(Model model) {

        // 서비스 계층의 selectAllActor 메서드 호출
        List<ActorVo> actors = actorService.selectAllActor();

        model.addAttribute("actors", actors);

        return "selectAllActor";

    }

    // 배우 검색
    @GetMapping("/searchActor")
    public String searchActor(
            // 뷰의 keyword를 파라미터로
            @RequestParam(value = "keyword", required = false) String keyword,
            // 검색 항목을 actor로
            @RequestParam(value = "searchCategory", defaultValue = "actor") String searchCategory,
            Model model, HttpSession session) {
        // ActorVo를 List형태로 searchResults에 할당
        List<ActorVo> searchResults;

        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 검색어가 비어있지 않은 경우
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 서비스계층의 keyword를 파라미터로 하는 배우 검색 메서드 호출
            searchResults = actorService.searchActor(keyword);
            model.addAttribute("keyword", keyword);

            // searchResults가 null 일 경우
            if (searchResults.isEmpty()) {
                model.addAttribute("searchMessage", "'" + keyword + "' 에 대한 검색 결과가 없습니다.");
            } else { // 반대의 경우
                model.addAttribute("searchMessage", "'" + keyword + "' 에 대한 검색 결과입니다.");
            }
        } else {
            return "redirect:/main";
        }

        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("actors", searchResults);

        return "searchActor";
    }

    @GetMapping("/updateActor/{actorNo}")
    public String updateActor(@PathVariable("actorNo") int actorNo, Model model, HttpSession session) {

        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // 관리자가 아니면 접근 불가
        if (loginUser == null || loginUser.getCommonNo() != 104) {
            return "redirect:/main";
        }

        // actorNo를 사용하여 DB에서 해당 배우의 정보를 가져옵니다.
        ActorVo actor = actorService.selectActor(actorNo);
        if (actor == null) {
            // 배우를 찾을 수 없을 때 처리
            // 예: 존재하지 않는 배우입니다 페이지로 이동 또는 목록으로 리다이렉트
            return "redirect:/actors";
        }
        model.addAttribute("actorVo", actor); // 가져온 배우 정보를 모델에 담아 뷰로 전달합니다.

        return "updateActor";
    }

    @PostMapping("/updateActor2")
    public String updateActor2(@ModelAttribute ActorVo actorVo, HttpSession session, RedirectAttributes rttr) {

        // 로그인 된 사용자의 정보를 변수 loginUser에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // 현재 로그인 된 사용자가 null 인지 관리자가 아닌지 확인하는 조건문
        if (loginUser == null || loginUser.getCommonNo() != 104) {
            rttr.addFlashAttribute("message", "관리자만 접근 가능");
            return "redirect:/main";
        }

        try {
            // 뷰에서 전송된 수정할 actorVo 정보를 담는다.
            // actorService의 updateActor 메서드 호출하여 해당 메서드가 성공적으로 작동됐을때
            // result에 변수 1이 할당된다.
            int result = actorService.updateActor(actorVo);
            
            if (result > 0) {
                rttr.addFlashAttribute("message", "배우 정보 수정 완료");
                
                return "redirect:/main";
            } else {
                
                rttr.addFlashAttribute("message", "배우 정보 수정 실패");
                
                return "redirect:/updateActor/" + actorVo.getActorNo();

            }
        } catch (Exception e) {

            e.printStackTrace();

            rttr.addFlashAttribute("message", "오류 발생");

            return "redirect:/updateActor/" + actorVo.getActorNo();
        }
    }

    // 배우 비공개 처리
    @PostMapping("/actorPrivate/{actorNo}")
    public String actorPriavte(@PathVariable("actorNo") int actorNo, 
        HttpSession session, RedirectAttributes redirectAttributes) {
        
        // 현재 세션을 변수 loginUser에 할당
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");

        // loginUser가 null이거나 loginUser가 104(관리자)가 아닐 경우
        if (loginUser == null || loginUser.getCommonNo() != 104) {
            redirectAttributes.addFlashAttribute("errorMessage", "관리자만 접근할 수 있는 기능입니다.");
            return "redirect:/main";
        }
        
        // 서비스 계층의 actorPrivate를 호출하여 변수 updatedRows에 할당
        int updatedRows = actorService.actorPrivate(actorNo);

        // updatedRows가 0보다 크면 영화 비공개처리 완료
        if (updatedRows > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "'" + actorNo + "' 배우가 성공적으로 비공개 처리되었습니다.");
        } else { // 그렇지 않다면 비공개처리 실패
            redirectAttributes.addFlashAttribute("errorMessage", "'" + actorNo + "' 배우 비공개 처리에 실패했거나 해당 배우를 찾을 수 없습니다.");
        }

        return "redirect:/main";
    }

}
