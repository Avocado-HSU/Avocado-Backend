package dismas.com.avocado.controller;

import dismas.com.avocado.dto.chatBotPage.ChatBotRequestType;
import dismas.com.avocado.dto.chatBotPage.ChatBotResponseDto;
import dismas.com.avocado.dto.parsingPage.WordEtymologyDto;
import dismas.com.avocado.dto.parsingPage.WordMeanDto;
import dismas.com.avocado.dto.parsingPage.WordSimilarDto;
import dismas.com.avocado.dto.parsingPage.WordTipsDto;
import dismas.com.avocado.mapper.ChatBotMapper;
import dismas.com.avocado.service.OpenAiService;
import dismas.com.avocado.service.ParsingService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "ChatBot API", description = "챗봇 아보카 관련 API")
public class ChatBotAPI {

    private final OpenAiService openAiService;
    private final WordService wordService;
    private final ChatBotMapper chatBotMapper;
    private final ParsingService parsingService;
    @PostMapping("api/test/getWordEtymology/{requestType}/{word}")
    public ResponseEntity<WordEtymologyDto> getWordEtymology(
            @PathVariable("requestType")
            ChatBotRequestType requestType,
            @PathVariable("word")
            String word
    ){
        String getvalue = openAiService.handleChatBotRequest(requestType, word);
        return ResponseEntity.ok(parsingService.parsingWordEtymology(getvalue));
    }
    @PostMapping("api/test/getWordSimilar/{requestType}/{word}")
    public ResponseEntity<WordSimilarDto> getWordSimilar(
            @PathVariable("requestType")
            ChatBotRequestType requestType,
            @PathVariable("word")
            String word
    ){
        String getvalue = openAiService.handleChatBotRequest(requestType, word);
        return ResponseEntity.ok(parsingService.parsingWordSimilar(getvalue));
    }

    @PostMapping("api/test/getWordMean/{requestType}/{word}")
    public ResponseEntity<WordMeanDto> getWordMean(
            @PathVariable("requestType")
            ChatBotRequestType requestType,
            @PathVariable("word")
            String word
    ){
        String getvalue = openAiService.handleChatBotRequest(requestType, word);
        return ResponseEntity.ok(parsingService.parsingWordMean(getvalue,word));

    }
    @PostMapping("api/test/getWordTips/{requestType}/{word}")
    public ResponseEntity<WordTipsDto> getWordTips(
            @PathVariable("requestType")
            ChatBotRequestType requestType,
            @PathVariable("word")
            String word
    ){
        String getvalue = openAiService.handleChatBotRequest(requestType, word);
        return ResponseEntity.ok(parsingService.parsingWordTips(getvalue));
    }


    @Operation(summary = "Request Word Info to ChatBot", description = "사용자가 챗봇에게 단어 관련 질문을 전달합니다. 이후 답을 반환받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "변수 입력 오류 - PathVariable Convert 실패"),
            @ApiResponse(responseCode = "404", description = "단어 검증 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("api/chatbot/request/{requestType}/{word}")
    public ResponseEntity<ChatBotResponseDto> handleRequest(
            @Parameter(name = "requestType", description = "챗봇에게 전달하고자 하는 질문 양식, ChatBotRequestType", example = "DEFINE", required = true)
            @Schema(allowableValues = {"DEFINE", "SIMILAR", "ETYMOLOGY", "PREFIX", "TIP"})
            @PathVariable("requestType")
            ChatBotRequestType requestType,

            @Parameter(name = "word", description = "검색하고자 하는 영어 단어 (한글 불가)", example = "hospitalization", required = true)
            @Schema(type = "string")
            @PathVariable("word")
            String word
    ){
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
