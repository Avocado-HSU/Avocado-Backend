package dismas.com.avocado.controller;


import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.mainPage.*;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.MainPageMapper;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
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
    @PostMapping("api/main/{id}")
    public ResponseEntity<MainPageResponseDto> getMainPage(
            @Parameter(description = "유저 id", required = true, example = "1")
            @PathVariable("id") Member member,
            @RequestBody MainPageRequestDto requestDto
    ) {
        MainPageCharacterDto mainPageCharacterDto;
        PopularWordDto popularWordDto;
        WeeklyAttendanceDto weeklyAttendanceDto;
        RecommendWordDto recommendWordDto;
        try {
            mainPageCharacterDto = new MainPageCharacterDto(characterService.getCharacterImage(member), "Hi" + member.getName());
            weeklyAttendanceDto = attendanceService.getWeeklyAttendance(member, requestDto.getDate());
            popularWordDto = popularWordService.getPopularWord();
            recommendWordDto = wordService.getRecommendWord();

        } catch (Exception e) {
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
    @GetMapping("api/main/{id}/search/{word}")
    public ResponseEntity<SearchWordResponseDto> searchWord(
            @Parameter(description = "유저 id", required = true, example = "1")
            @PathVariable("id") Member member,
            @Parameter(description = "단어", required = true, example = "단어")
            @PathVariable("word") String word){
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

    /**
     * MainPage Recent Search APi (검색 기록 API)
     * 가장 최근 검색한 내용 중 10개를 호출한다.
     * @param member 사용자 ID
     * @return RecentSearchWordResponseDto 최근 검색 단어 반환
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/main/{id}/search/recent")
    public RecentSearchWordResponseDto getRecentSearch(
            @Parameter(description = "유저 id", required = true, example = "1")
            @PathVariable("id") Member member
    ){
        return wordService.getRecentSearchWord(member);
    }

}
