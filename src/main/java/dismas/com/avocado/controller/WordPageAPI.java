package dismas.com.avocado.controller;
import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseDto;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.LibraryMapper;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.LibraryService;
import dismas.com.avocado.service.OpenAiService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WordPageAPI {
    private final WordService wordService;
    private final CharacterService characterService;
    private final OpenAiService openAiService;
    private final LibraryService libraryService;

    private final WordPageMapper wordPageMapper;
    private final LibraryMapper libraryMapper;


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
            @Parameter(description = "유저 id", required = true, example = "1")
            @PathVariable("id") Member member,
            @Parameter(description = "단어", required = true, example = "apple")
            @PathVariable("word") String word
    ) {

        if(wordService.validateWord(word)){
            try {
                String characterImgUrl = characterService.getCharacterImage(member);
                Map<SearchRequestType, String> contents = openAiService.handleSearchRequest(word);
                // 파싱 진행
                MemberWord memberWord = wordService.insertMemberWord(member, word, "", "");
                return ResponseEntity.ok(wordPageMapper.toSearchWordResponseDto(true, memberWord.getId(), characterImgUrl, contents));
            }catch (RuntimeException e){
                // OpenAI 에러
                return ResponseEntity.internalServerError().body(wordPageMapper.toSearchWordResponseDto(false, null, null, null));
            }
        }else{
            // 단어 검증 실패
            return ResponseEntity.badRequest().body(wordPageMapper.toSearchWordResponseDto(false, null,null, null));
        }
    }

    @PostMapping("api/word/library/{libraryId}")
    private ResponseEntity<UpdateLibraryResponseDto> updateLibrary(
            @Parameter(description = "라이브러리 id", required = true, example = "7")
            @PathVariable Long libraryId){
        UpdateLibraryResponseDto returnDto = libraryService.updateLibrary(libraryId);

        switch(returnDto.getResponseType()){
            case REGISTERED, DELETED -> {
                return ResponseEntity.ok(returnDto);
            }
            default -> {
                return ResponseEntity.internalServerError().body(returnDto);
            }
        }
    }


}