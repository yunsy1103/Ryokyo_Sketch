package com.travel.japan.controller;

import com.travel.japan.dto.ResponseDTO;
import com.travel.japan.entity.Member;
import com.travel.japan.entity.Notice;
import com.travel.japan.entity.NoticeImage;
import com.travel.japan.repository.MemberRepository;
import com.travel.japan.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Notice", description = "Notice API")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    private final MemberRepository memberRepository;


    // create board rest api
    @Operation(summary = "게시글 생성", description = "전체 게시글 생성")
    @PostMapping("/notice")
    public ResponseEntity<?> createNotice(@Validated @RequestPart("data")ResponseDTO responseDTO,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        // 이메일로 Member 엔티티 조회
        Member member = memberRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // 닉네임 가져오기
        String nickname = member.getNickname();

        Notice notice = new Notice();
        notice.setTitle(responseDTO.getTitle());
        notice.setContent(responseDTO.getContent());
        notice.setNickname(nickname);

        // Notice 엔티티와 이미지 파일을 함께 서비스 계층에 전달
        Notice savedNotice = noticeService.createNotice(notice, files);


        // 저장된 게시글 정보를 DTO로 반환
        ResponseDTO response = ResponseDTO.builder()
                .title(savedNotice.getTitle())
                .content(savedNotice.getContent())
                .nickname(savedNotice.getNickname())
                .imageUrls(savedNotice.getBoardImages().stream()
                        .map(NoticeImage::getUrl)
                        .collect(Collectors.toList()))
                .build();

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "게시글 조회", description = "전체 게시글을 조회")
    @GetMapping("/notice")
    public ResponseEntity<Page<Notice>>listAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Notice> notices = noticeService.listAllNotices(PageRequest.of(page, size));
        return ResponseEntity.ok(notices);
    }


    @Operation(summary = "게시글 검색", description = "해당 게시글 검색")
    @GetMapping("/notice/{id}")
    public ResponseEntity<Notice> getBoardById(@PathVariable Long id) {
        Notice notice = noticeService.getBoardById(id);
        return ResponseEntity.ok(notice);
    }


    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PutMapping("/notice/{id}")
    public ResponseEntity<Notice> updateBoard(@PathVariable Long id, @RequestBody Notice boardDetails) {
        Notice updatedNotice = noticeService.updateBoard(id, boardDetails);
        return ResponseEntity.ok(updatedNotice);
    }



    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/notice/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBoard(@PathVariable Long id) {
        Map<String, Boolean> response = noticeService.deleteBoard(id);
        return ResponseEntity.ok(response);
    }

}
