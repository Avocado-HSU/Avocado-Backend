package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.parsingPage.WordMultiDto;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "SearchPage", description = "검색 페이지 관련 API")
public class SearchPageAPI {

    private final WordService wordService;
    private final CharacterService characterService;

    private final WordPageMapper wordPageMapper;

    /**
     * SearchPage Search API (검색 API)
     * - 입력값에 대한 기본적인 형식 검증
     * - 비즈니스 로직과 관련된 심층 검증은 서비스 계층에서 수행
     * - 검색 서비스 호출
     * - 검색 성공 시 WordPageResponseDto 전달 -> WordPage 이동
     * - 검색 실패 시 WordPageResponseDto 에 false 값 담아서 전달
     * @param member 사용자 Entity
     * @param word 사용자가 입력한 검색값
     */

    @Operation(summary = "단어장 페이지로 이동", description = "검색에 대한 결과를 반환합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
            @ApiResponse(responseCode = "404", description = "잘못된 단어를 검색하였습니다 - 단어 검증 실패"),
            @ApiResponse(responseCode = "503", description = "OpenAI ERROR")
    })
    @GetMapping("api/search/{id}/{word}")
    public ResponseEntity<SearchWordResponseDto> wordSearch(
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            @PathVariable("id")
            @Schema(type = "integer", format = "int64")
            Member member,

            @Parameter(name = "word", description = "검색하고자 하는 영어 단어 (한글 불가)", example = "hospitalization", required = true)
            @PathVariable("word")
            String word
    ) {
        if(wordService.validateWord(word)){
            try {
                WordMultiDto contents = wordService.searchWord(member, word);

                String etymology = contents.getWordEtymologyDto().getEtymology();
                String korean = contents.getWordEtymologyDto().getKorean();
                String Suffix = contents.getWordEtymologyDto().getSuffix();
                String prefix = contents.getWordEtymologyDto().getPrefix();

                MemberWord createdWord = wordService.insertMemberWord(member, word, etymology, korean, Suffix, prefix);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(wordPageMapper.toSearchWordResponseDto(
                                true,
                                createdWord.getIsLibraryWord(),
                                createdWord.getId(),
                                characterService.getCharacterImage(member),
                                contents
                        ));
            }catch (RuntimeException e){
                // OpenAI 에러 : 503
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
        }else{
            // 단어 검증 실패
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
