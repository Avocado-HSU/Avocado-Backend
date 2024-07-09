package dismas.com.avocado.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatBotController {
    private final OpenAiChatModel chatModel;

    @Autowired
    public ChatBotController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // 초기 인사 메시지 반환
    @GetMapping("/default")
    public Map<String, String> getGreeting() {
        String greeting = "안녕하세요! 저는 사용자님의 영어 공부 도우미, ‘아보카’라고 합니다. 무엇을 도와드릴까요?";
        return Map.of("message", greeting);
    }

    // 사용자가 선택할 수 있는 옵션 반환
    @GetMapping("/options")
    public Map<String, String> getOptions() {
        String options = "뜻이 궁금한 단어가 있어!\n" +
                "이 단어에 유사 단어가 있는지 궁금해!\n" +
                "어원으로 단어를 분류해줘!\n" +
                "접두사/접미사가 같은 단어를 알려줘!\n" +
                "외우기 어려운 단어의 팁을 알려줘!";
        return Map.of("options", options);
    }

    // 5가지 조건에 해당하지 않는 요청의 경우 처리
    @PostMapping("/other-request")
    public Map<String, String> getOtherResponse(@RequestBody Map<String, String> requestBody) {
        String response = requestBody.get("otherRequest");
        return Map.of("response", response);
    }

    // 해당 단어의 뜻을 묻는 요청 처리 -> 뜻이 궁금한 단어가 있어!
    @PostMapping("/define")
    public Map<String, String> defineWord(@RequestBody Map<String, String> requestBody) {
        String word = requestBody.get("word");
        String prompt = "단어 '" + word + "'의 뜻을 알려주세요.";
        String definition = chatModel.call(prompt);
        String response = definition + " 더 필요한 도움이 있을까요?";
        return Map.of("response", response);
    }

    // 해당 단어와 유사한 단어를 찾는 요청 처리 -> 이 단어에 유사한 단어가 있는지 궁금해!
    @PostMapping("/similar-words")
    public Map<String, String> getSimilarWords(@RequestBody Map<String, String> requestBody) {
        String word = requestBody.get("word");
        String prompt = "단어 '" + word + "'과 유사한 단어를 알려주세요.";
        return Map.of("response", chatModel.call(prompt));
    }

    // 어원으로 단어를 분류해달라는 요청 처리 -> 어원으로 단어를 분류해줘!
    @PostMapping("/etymology")
    public Map<String, String> getEtymology(@RequestBody Map<String, String> requestBody) {
        String word = requestBody.get("word");
        String prompt = "단어 '" + word + "'의 어원을 알려주세요.";
        return Map.of("response", chatModel.call(prompt));
    }

    // 접두사/접미사가 같은 단어 요청 처리 -> 접두사/접미사가 같은 단어를 알려줘!
    @PostMapping("/prefix-suffix")
    public Map<String, String> getPrefixSuffix(@RequestBody Map<String, String> requestBody) {
        String prefixSuffix = requestBody.get("prefixSuffix");
        String prompt = "접두사/접미사 '" + prefixSuffix + "'가 같은 단어를 알려주세요.";
        return Map.of("response", chatModel.call(prompt));
    }

    // 외우기 어려운 단어에 대한 팁 요청 처리 -> 외우기 어려운 단어의 팁을 알려줘!
    @PostMapping("/memorization-tips")
    public Map<String, String> getMemorizationTips(@RequestBody Map<String, String> requestBody) {
        String word = requestBody.get("word");
        String prompt = "단어 '" + word + "'을 외우기 위한 팁을 알려주세요.";
        return Map.of("response", chatModel.call(prompt));
    }

    /*
    @GetMapping("/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }
    */
}
