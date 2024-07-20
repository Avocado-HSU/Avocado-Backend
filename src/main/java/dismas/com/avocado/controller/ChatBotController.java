package dismas.com.avocado.controller;

import dismas.com.avocado.dto.chatBotPage.ChatBotResponseDto;
import dismas.com.avocado.mapper.ChatBotMapper;
import dismas.com.avocado.service.OpenAiService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
                description = "greeting -> 환영인사, options -> 옵션 보기, 기타 요청은 아래에 있습니다."
            , tags = { "ChatBot Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("api/request")
    public ResponseEntity<ChatBotResponseDto> handleRequest(
            @Parameter(description = "요청 타입",required = true,examples = {
                    @ExampleObject(name = "define", value = "define", description = "단어의 정의 요청"),
                    @ExampleObject(name = "similar-words", value = "similar-words", description = "유사어 요청"),
                    @ExampleObject(name = "etymology", value = "etymology", description = "어원 요청"),
                    @ExampleObject(name = "prefix-suffix", value = "prefix-suffix", description = "접두사 요청"),
                    @ExampleObject(name = "memorization-tips", value = "memorization-tips", description = "기억 꿀팁 요청")
            })
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
                response = "Open AI Connect Error";
                return ResponseEntity.internalServerError().body(chatBotMapper.toChatBotResponseDto(isSuccess, response));
            }
        }else{
            response = "잘못된 단어를 요청하셨어요! 다시 입력해주세요!";
            return ResponseEntity.badRequest().body(chatBotMapper.toChatBotResponseDto(isSuccess, response));
        }

        return ResponseEntity.ok(chatBotMapper.toChatBotResponseDto(isSuccess, response));

    }
}
