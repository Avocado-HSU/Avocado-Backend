package dismas.com.avocado.controller;

import dismas.com.avocado.sevice.ChatBotService;
import org.springframework.ai.openai.OpenAiChatModel;
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

    @PostMapping("/request")
    public Map<String, String> handleRequest(@RequestBody Map<String, String> requestBody) {
        String requestType = requestBody.get("requestType");
        String content = requestBody.get("content");
        String response;

        if (requestType.equals("greeting")) {
            response = chatBotService.getGreeting();
        } else if (requestType.equals("options")) {
            response = chatBotService.getOptions();
        } else {
            response = chatBotService.handleRequest(requestType, content);
        }

        return Map.of("response", response);
    }
}
