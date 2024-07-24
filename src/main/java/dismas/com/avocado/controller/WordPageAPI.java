package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseDto;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import dismas.com.avocado.service.LibraryService;
import dismas.com.avocado.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WordPageAPI {
    private final WordService wordService;
    private final LibraryService libraryService;


    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "검색 페이지로 이동", description = "단어장 페이지에서 검색 창을 사용할 경우 호출합니다. 최근 검색 기록에 대해 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다")
    })
    @GetMapping("api/word/{id}/search")
    public RecentSearchWordResponseDto getRecentSearch(
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            @PathVariable("id")
            @Schema(type = "integer", format = "int64")
            Member member
    ){
        return wordService.getRecentSearchWord(member);
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