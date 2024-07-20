package dismas.com.avocado.controller;


import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.mainPage.*;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import dismas.com.avocado.mapper.MainPageMapper;
import dismas.com.avocado.service.AttendanceService;
import dismas.com.avocado.service.PopularWordService;
import dismas.com.avocado.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainPageAPI {

    private final MainPageMapper mainPageMapper;

    private final WordService wordService;
    private final AttendanceService attendanceService;
    private final PopularWordService popularWordService;

    /**
     * MainPage API
     * - 캐릭터 이미지 및 캐릭터 텍스트 반환
     * - 출석 관련 데이터 반환 (n월 m주차, 몇요일 출석)
     * - 인기 검색어 (최대 5개 반환)
     * - 추천 단어 반환
     * @param member 사용자
     */
    @GetMapping("api/main/{id}")
    public ResponseEntity<MainPageResponseDto> getMainPage(
            @PathVariable("id") Member member,
            @RequestBody MainPageRequestDto requestDto
    ) {
        PopularWordDto popularWordDto;
        WeeklyAttendanceDto weeklyAttendanceDto;
        RecommendWordDto recommendWordDto;
        try {
            weeklyAttendanceDto = attendanceService.getWeeklyAttendance(member, requestDto.getDate());
            popularWordDto = popularWordService.getPopularWord();
            recommendWordDto = wordService.getRecommendWord();

        } catch (Exception e) {
            log.error("출석 체크 서비스 에러 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mainPageMapper.toMainPageResponseDto(
                            new WeeklyAttendanceDto(),
                            new PopularWordDto(),
                            new RecommendWordDto()
                    ));
        }

        return ResponseEntity.ok(
                mainPageMapper.toMainPageResponseDto(
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
    @GetMapping("api/main/{id}/search/{word}")
    public void searchWord(
            @PathVariable("id") Member member,
            @PathVariable("word") String word){
        //WordPageResponseDto 반환
        //return wordService.searchWord(member, word);
    }

    /**
     * MainPage Recent Search APi (검색 기록 API)
     * 가장 최근 검색한 내용 중 10개를 호출한다.
     * @param member 사용자 ID
     * @return RecentSearchWordResponseDto 최근 검색 단어 반환
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/main/{id}/search/recent")
    public RecentSearchWordResponseDto getRecentSearch(
            @PathVariable("id") Member member
    ){
        return wordService.getRecentSearchWord(member);
    }

}
