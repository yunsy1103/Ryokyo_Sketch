package com.travel.japan.controller;

import com.travel.japan.dto.GPTResponseDTO;
import com.travel.japan.dto.QuestionDTO;
import com.travel.japan.service.GPTService;
import com.travel.japan.util.APIResponse;
import com.travel.japan.util.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@CrossOrigin(origins = "*")  // 모든 출처 허용
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
@Tag(name = "GPT", description = "GPT API")
public class GPTController {

    private final APIResponse apiResponse;
    private final GPTService gptService;

    @Operation(summary = "GPT 질문", description = "Chat-Gpt 질문 기능")
    @PostMapping("/question")
    public ResponseEntity sendQuestion(
            Locale locale,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody QuestionDTO questionDTO) {
        String code = ResponseCode.CD_SUCCESS;
        GPTResponseDTO gptResponseDTO = null;
        try {
            gptResponseDTO = gptService.askQuestion(questionDTO);
        } catch (Exception e) {
            apiResponse.printErrorMessage(e);
            code = e.getMessage();
        }
        HttpStatus status = code.equals(ResponseCode.CD_SUCCESS) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(gptResponseDTO);
    }
}



