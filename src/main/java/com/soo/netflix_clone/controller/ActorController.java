package com.soo.netflix_clone.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.soo.netflix_clone.vo.ActorVo;

@Controller
public class ActorController {

    // Actor서비스 자동 주입
    @Autowired
    private ActorServiceImpl actorService;

    // application.properties의 'file.upload-dir' 값을 주입
    // 이 경로는 File 객체 생성 시 문자열로 사용
    @Value("${file.upload-dir}")
    private String uploadDir;


    // 배우 조회
    @GetMapping("/actors/{actorNo}")
    public String selectActor(@PathVariable("actorNo") int actorNo, Model model) {

        // 서비스의 selectActor메서드 호출
        ActorVo actor = actorService.selectActor(actorNo);
        model.addAttribute("actor", actor);

        return "actorinfo";
    }

    // 배우 등록
    @GetMapping("/insertActor")
    public String insertActor(Model model) {
        model.addAttribute("actorVo", new ActorVo()); 
        return "insertActor";
    }

    // 배우 등록 Post
    @PostMapping("/insertActor2")
    public String insertActor2(
        @ModelAttribute ActorVo actorVo,
        @RequestParam("actorPosterFile") MultipartFile posterFile,
        RedirectAttributes redirectAttributes 
    ) {

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
                    try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
                }
                if (outputStream != null) {
                    try { outputStream.close(); } catch (IOException e) { e.printStackTrace(); }
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


}
