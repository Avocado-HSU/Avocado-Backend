package dismas.com.avocado.controller;


import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.mainPage.*;
import dismas.com.avocado.dto.parsingPage.WordMultiDto;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import dismas.com.avocado.dto.wordPage.SearchRequestType;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.MainPageMapper;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.*;
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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "MainPage", description = "메인 페이지 관련 API")
public class MainPageAPI {

    private final MainPageMapper mainPageMapper;
    private final WordPageMapper wordPageMapper;

    private final WordService wordService;
    private final AttendanceService attendanceService;
    private final PopularWordService popularWordService;
    private final CharacterService characterService;
    private final OpenAiService openAiService;

    /**
     * MainPage API
     * - 캐릭터 이미지 및 캐릭터 텍스트 반환
     * - 출석 관련 데이터 반환 (n월 m주차, 몇요일 출석)
     * - 인기 검색어 (최대 5개 반환)
     * - 추천 단어 반환
     * @param member 사용자
     */
    @Operation(summary = "Get MainPage Response Info", description = "메인 페이지를 렌더링하기 위한 정보를 제공합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
            @ApiResponse(responseCode = "500", description = "OpenAI ERROR")
    })
    @PostMapping("api/main/{id}")
    public ResponseEntity<MainPageResponseDto> getMainPage(
            @PathVariable("id")
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            @Schema(type = "integer", format = "int64")
            Member member,
            @RequestBody MainPageRequestDto requestDto
    ) {
        MainPageCharacterDto mainPageCharacterDto;
        PopularWordDto popularWordDto;
        WeeklyAttendanceDto weeklyAttendanceDto;
        RecommendWordDto recommendWordDto;

        try {
            // MainPage Mapper를 통해 처리 필요
            mainPageCharacterDto = new MainPageCharacterDto(characterService.getCharacterImage(member), "Hi" + member.getName());
            weeklyAttendanceDto = attendanceService.getWeeklyAttendance(member, requestDto.getDate());
            popularWordDto = popularWordService.getPopularWord();
            recommendWordDto = wordService.getRecommendWord();

        } catch (Exception e) {
            // 에러를 보다 세분화해서 처리할 필요 잇음
                // 1. OpenAI에서 발생하는 에러 처리 필요
                // 2. 출석체크에서 간혈적으로 발생하는 에러 처리 필요
            log.error("출석 체크 서비스 에러 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mainPageMapper.toMainPageResponseDto(
                            new MainPageCharacterDto(),
                            new WeeklyAttendanceDto(),
                            new PopularWordDto(),
                            new RecommendWordDto()
                    ));
        }

        return ResponseEntity.ok(
                mainPageMapper.toMainPageResponseDto(
                        mainPageCharacterDto,
                        weeklyAttendanceDto,    // 주간 출석 체크
                        popularWordDto,         // 인기 검색어
                        recommendWordDto        // 추천 검색어
                )
        );
    }

    /**
     * MainPage Search API (검색 API)
     * - 입력값에 대한 기본적인 형식 검증
     * - 비즈니스 로직과 관련된 심층 검증은 서비스 계층에서 수행
     * - 검색 서비스 호출
     * - 검색 성공 시 WordPageResponseDto 전달 -> WordPage 이동
     * - 검색 실패 시 WordPageResponseDto 에 false 값 담아서 전달
     * @param member 사용자 Entity
     * @param word 사용자가 입력한 검색값
     */

    @Operation(summary = "Get Search Result at MainPage", description = "메인 페이지에서 검색 창을 사용할 경우 호출합니다. WordPage 에 대한 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다"),
            @ApiResponse(responseCode = "404", description = "잘못된 단어를 검색하였습니다 - 단어 검증 실패"),
            @ApiResponse(responseCode = "503", description = "OpenAI ERROR")
    })
    @GetMapping("api/main/{id}/search/{word}")
    public ResponseEntity<SearchWordResponseDto> wordSearch(
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            @PathVariable("id")
            @Schema(type = "integer", format = "int64")
            Member member,

            @Parameter(name = "word", description = "검색하고자 하는 영어 단어 (한글 불가)", example = "hospitalization", required = true)
            @PathVariable("word")
            @Schema(type = "integer", format = "int64")
            String word
    ) {
        if(wordService.validateWord(word)){
            try {
                WordMultiDto contents = wordService.searchWord(member, word);
                //wordService.parsingWord(contents);

                MemberWord createdWord = wordService.insertMemberWord(member, word, contents.getWordEtymologyDto()
                        .getEtymology(), "한글 뜻",contents.getWordEtymologyDto().getSuffix());

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

    /**
     * MainPage Recent Search APi (검색 기록 API)
     * 가장 최근 검색한 내용 중 10개를 호출한다.
     * @param member 사용자 ID
     * @return RecentSearchWordResponseDto 최근 검색 단어 반환
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Recent Search Info", description = "사용자가 최근 검색한 단어 10개를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다")
    })
    @GetMapping("api/main/{id}/search/recent")
    public RecentSearchWordResponseDto getRecentSearch(
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            @PathVariable("id")
            @Schema(type = "integer", format = "int64")
            Member member
    ){
        return wordService.getRecentSearchWord(member);
    }

}
