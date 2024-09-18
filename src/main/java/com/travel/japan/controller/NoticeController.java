package com.travel.japan.controller;

import com.travel.japan.dto.ResponseDTO;
import com.travel.japan.entity.Notice;
import com.travel.japan.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://3.36.49.115:8081")
@RequestMapping("/api")
@Tag(name = "Notice", description = "Notice API")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // create board rest api
    @Operation(summary = "게시글 생성", description = "전체 게시글 생성")
    @PostMapping("/notice")
    public ResponseEntity<?> createNotice(@Validated @RequestBody ResponseDTO responseDTO) {
        Notice notice = new Notice();
        notice.setTitle(responseDTO.getTitle());
        notice.setContent(responseDTO.getContent());
        notice.setViewCnt(responseDTO.getViewCnt());

        return ResponseEntity.ok(noticeService.createNotice(notice));
    }

    // list all boards
    @Operation(summary = "게시글 조회", description = "전체 게시글을 조회")
    @GetMapping("/notice")
    public Page<Notice> listAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return noticeService.listAllNotices(PageRequest.of(page, size));
    }

    // get board by id
    @Operation(summary = "게시글 검색", description = "해당 게시글 검색")
    @GetMapping("/notice/{id}")
    public ResponseEntity<Notice> getBoardById(@PathVariable Integer id) {
        return noticeService.getBoardById(id);
    }

    // update board
    @Operation(summary = "게시글 수정", description = "해당 게시글 수정")
    @PutMapping("/notice/{id}")
    public ResponseEntity<Notice> updateBoard(
            @PathVariable Integer id, @RequestBody Notice boardDetails) {
        return noticeService.updateBoard(id, boardDetails);
    }

    // delete board
    @Operation(summary = "게시글 삭제", description = "해당 게시글 삭제")
    @DeleteMapping("/notice/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBoard(@PathVariable Integer id) {
        return noticeService.deleteBoard(id);
    }

}
