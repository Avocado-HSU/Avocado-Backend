package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.LibraryPageResponseDto;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseDto;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseType;
import dismas.com.avocado.dto.wordPage.SearchRequestType;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.LibraryMapper;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.LibraryService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseType.DELETED;
import static dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseType.REGISTERED;

@RestController
@RequiredArgsConstructor
@Tag(name = "LibraryPage", description = "라이브러리 페이지 관련 API")
public class LibraryPageAPI {

    private final LibraryMapper libraryMapper;
    private final WordPageMapper wordPageMapper;

    private final CharacterService characterService;
    private final LibraryService libraryService;
    private final WordService wordService;

    /**
     * Library Page API
     *  1. 캐릭터 이미지 반환
     *  2. 라이브러리 단어 반환
     *      - LibraryResponseDto의 size가 0일 시, client에서 별도의 안내가 필요 (라이브러리 단어를 지정해 보세요!)
     * @param member 사용자 ID
     * @return LibraryPageResponseDto 라이브러리 페이지를 구성하기 위한 DTO 반환
     */
    @Operation(summary = "Get Library Page Response Info", description = "라이브러리 페이지를 렌더링하기 위한 정보를 제공합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/library/{id}")
    public LibraryPageResponseDto getLibraryPage(
            @PathVariable("id")
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            Member member
    ){
        return libraryMapper.toLibraryPageDto(
                libraryService.getLibrary(member),
                characterService.getCharacterImage(member));
    }


    /**
     * Delete Library Word API (라이브러리 단어 삭제 API)
     * @param libraryId 라이브러리 단어 ID (서버에서 Library Page 요청 시 제공)
     */
    @Operation(summary = "Delete Library Word", description = "라이브러리 단어를 삭제합니다. (현재 기획에 X, 2차 MVP)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
            @ApiResponse(responseCode = "500", description = "라이브러리 삭제 실패")
    })
    @DeleteMapping("api/library/{libraryId}")
    public ResponseEntity<UpdateLibraryResponseDto> deleteLibraryWord(
            @PathVariable("libraryId")
            @Parameter(name = "libraryId", description = "라이브러리에 등록된 단어 ID, Long Type", example = "3", required = true)
            @Schema(type = "integer", format = "int64")
            Long libraryId
    ){
        UpdateLibraryResponseDto returnDto = libraryService.updateLibrary(libraryId);
        UpdateLibraryResponseType responseType = returnDto.getResponseType();

        if (responseType == REGISTERED || responseType == DELETED) {
            return ResponseEntity.ok(returnDto);
        } else {
            return ResponseEntity.internalServerError().body(returnDto);
        }
    }

    /**
     * Library Page Search API (라이브러리 검색 API)
     * - 라이브러리 페이지에서 검색창 이용시 호출
     * - 라이브러리 단어 검색 시 호출
     *
     * @param member 사용자 ID
     * @param word 라이브러리 단어 ID (서버에서 Library Page 요청 시 제공)
     */
    @Operation(summary = "Get Search Result at LibraryPage", description = "라이브러리 페이지에서 검색 창을 사용할 경우 호출합니다. WordPage 에 대한 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
            @ApiResponse(responseCode = "404", description = "잘못된 단어를 검색하였습니다 - 단어 검증 실패"),
            @ApiResponse(responseCode = "503", description = "OpenAI ERROR")
    })
    @GetMapping("api/library/{id}/search/{word}")
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
                Map<SearchRequestType,String> contents = wordService.searchWord(member, word);
                //wordService.parsingWord(contents);
                MemberWord createdWord = wordService.insertMemberWord(member, word, "어원", "한글 뜻");

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
