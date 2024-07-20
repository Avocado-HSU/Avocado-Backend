package dismas.com.avocado.controller;

import dismas.com.avocado.service.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @Autowired
    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @Operation(summary = "챗봇에 정보 요청",
                description = "greeting -> 환영인사, options -> 옵션 보기, 기타 요청은 아래에 있습니다."
            , tags = { "ChatBot Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })

    @PostMapping("/request")
    public Map<String, String> handleRequest(
            @Parameter(description = "요청 타입",required = true,examples = {
                    @ExampleObject(name = "greeting", value = "greeting", description = "인사 요청"),
                    @ExampleObject(name = "options", value = "options", description = "옵션 요청"),
                    @ExampleObject(name = "define", value = "define", description = "단어의 정의 요청"),
                    @ExampleObject(name = "similar-words", value = "similar-words", description = "유사어 요청"),
                    @ExampleObject(name = "etymology", value = "etymology", description = "어원 요청"),
                    @ExampleObject(name = "prefix-suffix", value = "prefix-suffix", description = "접두사,접미사 요청"),
                    @ExampleObject(name = "memorization-tips", value = "memorization-tips", description = "기억 꿀팁 요청")
            })
            @RequestParam("requestType") String requestType,
            @Parameter(description = "단어",required = false,example = "apple")
            @RequestParam("content") String content) {

        String response;

        if ("greeting".equals(requestType)) {
            response = chatBotService.getGreeting();
        } else if ("options".equals(requestType)) {
            response = chatBotService.getOptions();
        } else {
            response = chatBotService.handleRequest(requestType, content);
        }

        return Map.of("response", response);
    }
}
