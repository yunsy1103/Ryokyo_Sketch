package com.travel.japan.service;

import com.travel.japan.entity.Notice;
import com.travel.japan.entity.NoticeImage;
import com.travel.japan.exception.ResourceNotFoundException;
import com.travel.japan.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    private static final String UPLOAD_DIR = "/var/www/uploads"; // 서버에 저장할 경로
    // 게시글 생성
    public Notice createNotice(Notice notice, List<MultipartFile> files) {
        // 이미지 파일이 있을 경우 업로드 처리
        if (files != null && !files.isEmpty()) {
            List<NoticeImage> noticeImages = files.stream()
                    .map(file -> new NoticeImage(null, uploadImage(file), notice))
                    .collect(Collectors.toList());
            notice.setBoardImages(noticeImages); // 이미지 리스트 설정
        }

        // 게시글 저장
        return noticeRepository.save(notice);
    }

    // 이미지 업로드 처리
    public String uploadImage(MultipartFile file) {
        try {
            // 파일 저장 경로 설정
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = UPLOAD_DIR + File.separator + fileName;

            // 파일을 서버에 저장
            Files.copy(file.getInputStream(), Paths.get(filePath));

            // 파일이 저장된 경로를 URL로 반환
            return "/uploads/" + fileName; // 클라이언트가 접근할 수 있는 URL 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }



    // 전체 게시글 조회
    public Page<Notice> listAllNotices(Pageable pagable) {
        return noticeRepository.findAll(pagable);
    }


    // 게시글 ID로 조회
    public Notice getBoardById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));
    }

    // 게시글 수정
    public Notice updateBoard(Long id, Notice boardDetails) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));

        notice.setTitle(boardDetails.getTitle());
        notice.setContent(boardDetails.getContent());

        return noticeRepository.save(notice);
    }

    // 게시글 삭제
    public Map<String, Boolean> deleteBoard(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));

        // 게시글에 연결된 이미지 삭제 처리 (파일 시스템에서 삭제)
        for (NoticeImage image : notice.getBoardImages()) {
            File file = new File(UPLOAD_DIR + File.separator + image.getUrl().replace("/uploads/", ""));
            if (file.exists()) {
                file.delete();
            }
        }

        noticeRepository.delete(notice);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}