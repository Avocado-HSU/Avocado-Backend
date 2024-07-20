package dismas.com.avocado.service;

import dismas.com.avocado.controller.ChatBotRequestType;
import dismas.com.avocado.controller.SearchRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dismas.com.avocado.controller.ChatBotRequestType.TIP;
import static dismas.com.avocado.controller.SearchRequestType.MEANS;
import static dismas.com.avocado.controller.SearchRequestType.SEPARATE;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private OpenAiChatModel openAiChatModel;

    public Map<SearchRequestType, String> handleSearchRequest(String word){
        Map<SearchRequestType,String> contents = new ConcurrentHashMap<>();

        contents.put(MEANS, getSearchMeans(word));
        contents.put(SEPARATE, getSearchSeparateEtymology(word));
        contents.put(SearchRequestType.TIP, getSearchTip(word));

        return contents;
    }

    private String getSearchMeans(String word) {
        String prompt = "단어 '" + word + "'의 뜻을 알려주세요.";
        return openAiChatModel.call(prompt) + " 더 필요한 도움이 있을까요?";
    }

    private String getSearchSeparateEtymology(String word) {
        String prompt = "단어 '" + word + "'의 어원을 알려주세요.";
        return openAiChatModel.call(prompt);
    }

    private String getSearchTip(String word) {
        String prompt = "단어의 어원을 기반으로 '" + word + "'을 외우기 위한 팁을 알려주세요.";
        // 파싱 후 반환 (parsing service)
        return openAiChatModel.call(prompt);
    }

    public String handleChatBotRequest(ChatBotRequestType requestType, String word) {
        return switch (requestType) {
            case DEFINE -> getMeans(word);
            case SIMILAR -> getSimilarWords(word);
            case ETYMOLOGY -> getEtymology(word);
            case PREFIX -> getPrefix(word);
            case TIP -> getTips(word);
            default -> throw new RuntimeException("OpenAI 오류");
        };
    }

    private String getMeans(String word) {
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

    private String getPrefix(String prefixSuffix) {
        String prompt = "접두사/접미사 '" + prefixSuffix + "'가 같은 단어를 알려주세요.";
        return openAiChatModel.call(prompt);
    }

    private String getTips(String word) {
        String prompt = "단어 '" + word + "'을 외우기 위한 팁을 알려주세요.";
        // 파싱 후 반환 (parsing service)
        return openAiChatModel.call(prompt);
    }




}
