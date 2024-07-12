package dismas.com.avocado.sevice;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {
    private OpenAiChatModel openAiChatModel;

    @Autowired
    public ChatBotService(OpenAiChatModel openAiChatModel){
        this.openAiChatModel=openAiChatModel;
    }
    public String getGreeting() {
        return "안녕하세요! 저는 사용자님의 영어 공부 도우미, ‘아보카’라고 합니다. 무엇을 도와드릴까요?";
    }

    public String getOptions() {
        return "뜻이 궁금한 단어가 있어!\n" +
                "이 단어에 유사 단어가 있는지 궁금해!\n" +
                "어원으로 단어를 분류해줘!\n" +
                "접두사/접미사가 같은 단어를 알려줘!\n" +
                "외우기 어려운 단어의 팁을 알려줘!";
    }

    public String handleRequest(String requestType, String content) {
        switch (requestType) {
            case "define":
                return defineWord(content);
            case "similar-words":
                return getSimilarWords(content);
            case "etymology":
                return getEtymology(content);
            case "prefix-suffix":
                return getPrefixSuffix(content);
            case "memorization-tips":
                return getMemorizationTips(content);
            default:
                return "요청을 이해하지 못했습니다. 다시 시도해주세요.";
        }
    }

    private String defineWord(String word) {
        String prompt = "단어 '" + word + "'의 뜻을 알려주세요.";
        return openAiChatModel.call(prompt) + " 더 필요한 도움이 있을까요?";
    }

    private String getSimilarWords(String word) {
        String prompt = "단어 '" + word + "'과 유사한 단어를 알려주세요.";
        return openAiChatModel.call(prompt);
    }

    private String getEtymology(String word) {
        String prompt = "단어 '" + word + "'의 어원을 알려주세요.";
        return openAiChatModel.call(prompt);
    }

    private String getPrefixSuffix(String prefixSuffix) {
        String prompt = "접두사/접미사 '" + prefixSuffix + "'가 같은 단어를 알려주세요.";
        return openAiChatModel.call(prompt);
    }

    private String getMemorizationTips(String word) {
        String prompt = "단어 '" + word + "'을 외우기 위한 팁을 알려주세요.";
        return openAiChatModel.call(prompt);
    }

}
