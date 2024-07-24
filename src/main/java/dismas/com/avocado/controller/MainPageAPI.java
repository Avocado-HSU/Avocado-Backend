package dismas.com.avocado.controller;


import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.mainPage.*;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import dismas.com.avocado.mapper.MainPageMapper;
import dismas.com.avocado.service.AttendanceService;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.PopularWordService;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "MainPage", description = "메인 페이지 관련 API")
public class MainPageAPI {

    private final MainPageMapper mainPageMapper;
    private final WordService wordService;
    private final AttendanceService attendanceService;
    private final PopularWordService popularWordService;
    private final CharacterService characterService;

    /**
     * MainPage API
     * - 캐릭터 이미지 및 캐릭터 텍스트 반환
     * - 출석 관련 데이터 반환 (n월 m주차, 몇요일 출석)
     * - 인기 검색어 (최대 5개 반환)
     * - 추천 단어 반환
     * @param member 사용자
     */
    @Operation(summary = "메인 페이지로 이동", description = "메인 페이지를 렌더링하기 위한 정보를 제공합니다")
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
        attendanceService.checkAttendance(member, requestDto.date);

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
     * Get Search APi (검색 페이지 호출 API)
     * 가장 최근 검색한 내용 중 10개를 호출한다.
     * @param member 사용자 ID
     * @return RecentSearchWordResponseDto 최근 검색 단어 반환
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "검색 페이지로 이동", description = "메인 페이지에서 검색 창을 사용할 경우 호출합니다. 최근 검색 기록에 대해 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "해당 ID의 유저가 존재하지 않습니다")
    })
    @GetMapping("api/main/{id}/search")
    public RecentSearchWordResponseDto getRecentSearch(
            @Parameter(name = "id", description = "로그인 유저 ID, Long Type", example = "3", required = true)
            @PathVariable("id")
            @Schema(type = "integer", format = "int64")
            Member member
    ){
        return wordService.getRecentSearchWord(member);
    }

}
