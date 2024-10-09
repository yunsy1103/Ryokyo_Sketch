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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

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
        // 이미지 파일 저장 로직 (예: 로컬 디렉토리 또는 AWS S3에 저장)
        // 여기에서는 간단히 파일 이름을 URL로 반환하는 예시를 제공함
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        // 실제 이미지 파일 저장은 생략 (실제 환경에서는 S3나 파일 시스템에 저장 필요)
        return "/images/" + fileName; // 저장된 이미지의 URL 반환
    }


    // list all boards
    public Page<Notice> listAllNotices(Pageable pagable) {
        return noticeRepository.findAll(pagable);
    }


    // get board by id
    public ResponseEntity<Notice> getBoardById(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));

        return ResponseEntity.ok(notice);
    }
    // update board
    public ResponseEntity<Notice> updateBoard(@PathVariable Long id, @RequestBody Notice boardDetails) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));

        notice.setTitle(boardDetails.getTitle());
        notice.setContent(boardDetails.getContent());

        Notice updatedBoard = noticeRepository.save(notice);
        return ResponseEntity.ok(updatedBoard);
    }

    // delete board
    public ResponseEntity<Map<String, Boolean>> deleteBoard(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not exist with id :" + id));
        noticeRepository.delete(notice);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}