package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.OpenAiService;
import dismas.com.avocado.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WordPageAPI {
    private final WordService wordService;
    private final CharacterService characterService;
    private final OpenAiService openAiService;
    private final WordPageMapper wordPageMapper;


    /**
     * Search API
     * - 입력값에 대한 기본적인 형식 검증
     * - 비즈니스 로직과 관련된 심층 검증은 서비스 계층에서 수행
     * - 검색 서비스 호출
     * - 검색 성공 시 WordPageResponseDto 전달
     * - 검색 실패 시 WordPageResponseDto 에 false 값 담아서 전달
     * @param member 사용자 Entity
     * @param word 사용자가 입력한 검색 값
     */
    @GetMapping("api/word/{id}/search/{word}")
    private ResponseEntity<SearchWordResponseDto> wordSearch(
            @PathVariable("id") Member member,
            @PathVariable("word") String word
    ) {

        if(wordService.validateWord(word)){
            try {
                String characterImgUrl = characterService.getCharacterImage(member);
                Map<SearchRequestType, String> contents = openAiService.handleSearchRequest(word);
                return ResponseEntity.ok(wordPageMapper.toSearchWordResponseDto(true, characterImgUrl, contents));
            }catch (RuntimeException e){
                // OpenAI 에러
                return ResponseEntity.internalServerError().body(wordPageMapper.toSearchWordResponseDto(false, null, null));
            }
        }else{
            // 단어 검증 실패
            return ResponseEntity.badRequest().body(wordPageMapper.toSearchWordResponseDto(false, null, null));
        }
    }

}
