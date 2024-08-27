package com.travel.japan.service;

import com.travel.japan.config.GPTConfig;
import com.travel.japan.dto.GPTRequestDTO;
import com.travel.japan.dto.GPTResponseDTO;
import com.travel.japan.dto.Message;
import com.travel.japan.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GPTService {
    private final RestTemplate restTemplate;

    @Value("${gpt.api.key}")
    private String apiKey;

    public HttpEntity<GPTRequestDTO> buildHttpEntity(GPTRequestDTO gptRequestDTO){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(GPTConfig.MEDIA_TYPE));
        httpHeaders.add(GPTConfig.AUTHORIZATION, GPTConfig.BEARER + apiKey);
        return new HttpEntity<>(gptRequestDTO, httpHeaders);
    }


    public GPTResponseDTO getResponse(HttpEntity<GPTRequestDTO> chatGptRequestHttpEntity){

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        //답변이 길어질 경우 TimeOut Error가 발생하니 1분정도 설정해줍니다.
        requestFactory.setReadTimeout(60 * 1000);   //  1min = 60 sec * 1,000ms
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<GPTResponseDTO> responseEntity = restTemplate.postForEntity(
                GPTConfig.CHAT_URL,
                chatGptRequestHttpEntity,
                GPTResponseDTO.class);

        return responseEntity.getBody();
    }

    public GPTResponseDTO askQuestion(QuestionDTO questionRequest){
        List<Message> messages = new ArrayList<>();
        messages.add(Message.builder()
                .role(GPTConfig.ROLE)
                .content(questionRequest.getQuestion())
                .build());
        try {
            // 이전 요청 후 1초 대기
            Thread.sleep(1000); // 1초 대기 후 다음 요청
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 필요 시 예외 처리 추가
        }

        return this.getResponse(
                this.buildHttpEntity(
                        new GPTRequestDTO(
                                GPTConfig.CHAT_MODEL,
                                GPTConfig.MAX_TOKEN,
                                GPTConfig.TEMPERATURE,
                                GPTConfig.STREAM,
                                messages
                                //ChatGptConfig.TOP_P
                        )
                )
        );
    }
}
