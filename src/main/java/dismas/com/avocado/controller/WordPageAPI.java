package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseDto;
import dismas.com.avocado.dto.parsingPage.WordMultiDto;
import dismas.com.avocado.dto.wordPage.SearchRequestType;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.LibraryService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final LibraryService libraryService;

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
    @Operation(summary = "Get Search Result at WordPage", description = "단어 페이지에서 검색 창을 사용할 경우 호출합니다. 입력된 Word에 대한 WordPage 에 대한 정보를 다시 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
            @ApiResponse(responseCode = "404", description = "잘못된 단어를 검색하였습니다 - 단어 검증 실패"),
            @ApiResponse(responseCode = "503", description = "OpenAI ERROR")
    })
    @GetMapping("api/word/{id}/search/{word}")
    public ResponseEntity<SearchWordResponseDto> wordSearch(
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            @PathVariable("id")
            @Schema(type = "integer", format = "int64")
            Member member,

            @Parameter(name = "word", description = "검색하고자 하는 영어 단어 (한글 불가)", example = "hospitalization", required = true)
            @PathVariable("word")
            @Schema(type = "string")
            String word
    ) {
        if(wordService.validateWord(word)){
            try {
                //변경 24-07-25
                WordMultiDto contents = wordService.searchWord(member, word);
                //wordService.parsingWord(contents);
                MemberWord createdWord = wordService.insertMemberWord(member, word, contents.getWordEtymologyDto().
                        getEtymology(), "한글 뜻",contents.getWordEtymologyDto().getSuffix());

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




    @Operation(summary = "Update Library Word", description = "라이브러리 단어의 등록/삭제를 수행합니다. 토글 형식")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
            @ApiResponse(responseCode = "503", description = "라이브러리 단어 삭제 실패")
    })
    @PostMapping("api/word/library/{libraryId}")
    private ResponseEntity<UpdateLibraryResponseDto> updateLibrary(
            @PathVariable("libraryId")
            @Parameter(name = "libraryId", description = "라이브러리에 등록된 단어 ID, Long Type", example = "3", required = true)
            @Schema(type = "integer", format = "int64")
            Long libraryId){
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