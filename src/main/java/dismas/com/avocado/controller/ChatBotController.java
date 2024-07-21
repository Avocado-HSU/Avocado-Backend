package dismas.com.avocado.controller;

import dismas.com.avocado.dto.chatBotPage.ChatBotResponseDto;
import dismas.com.avocado.mapper.ChatBotMapper;
import dismas.com.avocado.service.OpenAiService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatBotController {

    private final OpenAiService openAiService;
    private final WordService wordService;
    private final ChatBotMapper chatBotMapper;

    @Operation(summary = "챗봇에 정보 요청",
                description = "챗봇에게 요청하고 응답받기"
            , tags = { "ChatBot Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("api/request")
    public ResponseEntity<ChatBotResponseDto> handleRequest(
            @RequestParam("requestType") ChatBotRequestType requestType,
            @Parameter(description = "단어", required = false, example = "apple")
            @RequestParam("word") String word) {

        String response;
        boolean isSuccess = false;

        if(wordService.validateWord(word)){
            try{
                response = openAiService.handleChatBotRequest(requestType, word);
                isSuccess = true;
            }catch (RuntimeException e){
                response = String.valueOf(e);
                return ResponseEntity.internalServerError().body(chatBotMapper.toChatBotResponseDto(isSuccess, response));
            }
        }else{
            response = "잘못된 단어를 요청하셨어요! 다시 입력해주세요!";
            return ResponseEntity.badRequest().body(chatBotMapper.toChatBotResponseDto(isSuccess, response));
        }

        return ResponseEntity.ok(chatBotMapper.toChatBotResponseDto(isSuccess, response));

    }
}
