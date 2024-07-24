package dismas.com.avocado.service;

import dismas.com.avocado.dto.chatBotPage.ChatBotRequestType;
import dismas.com.avocado.dto.parsingPage.WordMultiDto;
import dismas.com.avocado.dto.parsingPage.WordTipsDto;
import dismas.com.avocado.dto.wordPage.SearchRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dismas.com.avocado.dto.wordPage.SearchRequestType.MEANS;
import static dismas.com.avocado.dto.wordPage.SearchRequestType.SEPARATE;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Autowired
    private ParsingService parsingService;

    @Autowired
    private OpenAiChatModel openAiChatModel;


    public WordMultiDto handleSearchRequest(String word){
      //  Map<SearchRequestType,String> contents = new ConcurrentHashMap<>();
        WordMultiDto wordMultiDto = new WordMultiDto();

        wordMultiDto.setWordMeanDto(parsingService.parsingWordMean(getMeans(word)));
        wordMultiDto.setWordEtymologyDto(parsingService.parsingWordEtymology(getEtymology(word)));
        wordMultiDto.setWordTipsDto(parsingService.parsingWordTips(getTips(word)));
        return wordMultiDto;
    }
/*
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
*/
    public String handleChatBotRequest(ChatBotRequestType requestType, String word) {
        return switch (requestType) {
            case DEFINE -> getMeans(word);
            case SIMILAR -> getSimilarWords(word);
            case ETYMOLOGY -> getEtymology(word);
           // case PREFIX -> getPrefix(word);
            case TIP -> getTips(word);
            default -> throw new RuntimeException("OpenAI 오류");
        };
    }

    private String getMeans(String word) {
        String prompt = "영단어 '"+word+"'의 품사별 뜻을 알려줘! 어떤 품사인지 설명하고 그 뜻과 설명을 작성해줘.";
        String result = openAiChatModel.call(prompt);
        System.out.println(result);
        return result;
    }

    private String getSimilarWords(String word) {
        String prompt = "영단어 '" + word + "'와 유사 단어를 3개만 알려주고 단어 3개의 한국어 뜻도 설명해줘 요구한 내용만 출력하고 쓸데 없는 얘기를 붙이지마";
        return openAiChatModel.call(prompt);
    }

    private String getEtymology(String word) {
        String prompt = "영단어 '"+word+"'를 어원으로 단어를 어원, 어근, 접두사, " +
                "접미사로 분류하여 설명하라. 각 분야는 ':'으로 분리하고 어떤부분이 어근이고 접두사" +
                "이고 접미사인지 적고 설명은 아래쪽에해. 만약 적절한 어원이나 접두사, 접미사가" +
                " 없다면 없음이라 표기하지 말고 빈칸으로 표기해. 만약 접두사나 접미사가 " +
                "없다면 빈칸으로만 표기하고 아래 설명에서는 그부분에 대한 설명을 생략해. 영단어는 한번 더 출력하지마.";
        String a = openAiChatModel.call(prompt);
        System.out.println(a);
        return a;
    }
/*
    private String getPrefix(String prefixSuffix) {
        String prompt = "영단어  '" + prefixSuffix + "'의 접두사/접미사가 접두사와 접미사가 같은 단어를 각각 3개씩 알려줘!";
        return openAiChatModel.call(prompt);
    }
*/
    private String getTips(String word) {
        String prompt = "영단어  '" + word + "'에 대해 단어 암기 방법을 아래를 참고하여 설명하라 연상 기억법, 단어 분해, 반복 학습, 스토리텔링 네가지 분야로 설명할 것. 분야별로 ':' 형태로 나눠서 설명할 것\n";
        String a=openAiChatModel.call(prompt);
        System.out.println(a);
        return a;
    }
}
