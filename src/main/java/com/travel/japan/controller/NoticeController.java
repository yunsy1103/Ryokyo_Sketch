package com.travel.japan.controller;

import com.travel.japan.dto.ResponseDTO;
import com.travel.japan.entity.Notice;
import com.travel.japan.service.NoticeService;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // create board rest api
    @ApiOperation(value = "게시글 생성", notes = "전체 게시글 생성")
    @PostMapping("/notice")
    public ResponseEntity<?> createNotice(@Validated @RequestBody ResponseDTO responseDTO) {
        Notice notice = new Notice();
        notice.setTitle(responseDTO.getTitle());
        notice.setContent(responseDTO.getContent());
        notice.setViewCnt(responseDTO.getViewCnt());

        return ResponseEntity.ok(noticeService.createNotice(notice));
    }

    // list all boards
    @ApiOperation(value = "게시글 조회", notes = "전체 게시글을 조회")
    @GetMapping("/notice")
    public Page<Notice> listAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return noticeService.listAllNotices(PageRequest.of(page, size));
    }

    // get board by id
    @ApiOperation(value = "게시글 검색", notes = "해당 게시글 검색")
    @GetMapping("/boards/{id}")
    public ResponseEntity<Notice> getBoardById(@PathVariable Integer id) {
        return noticeService.getBoardById(id);
    }

    // update board
    @ApiOperation(value = "게시글 수정", notes = "해당 게시글 수정")
    @PutMapping("/boards/{id}")
    public ResponseEntity<Notice> updateBoard(
            @PathVariable Integer id, @RequestBody Notice boardDetails) {
        return noticeService.updateBoard(id, boardDetails);
    }

    // delete board
    @ApiOperation(value = "게시글 삭제", notes = "해당 게시글 삭제")
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBoard(@PathVariable Integer id) {
        return noticeService.deleteBoard(id);
    }

}
