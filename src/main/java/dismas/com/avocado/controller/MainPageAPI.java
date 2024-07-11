package dismas.com.avocado.controller;


import dismas.com.avocado.domain.Member;
import dismas.com.avocado.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainPageAPI {

    private final WordService wordService;

    /**
     * MainPage API
     * - 캐릭터 이미지 및 캐릭터 텍스트 반환
     * - 출석 관련 데이터 반환 (n월 m주차, 몇요일 출석)
     * - 인기 검색어 (최대 5개 반환)
     * - 추천 단어 반환
     * @param member
     */
    @GetMapping("api/main/{id}")
    public void getMainPage(@PathVariable("id") Member member){
        // 캐릭터 서비스 호출 및 캐릭터 반환

        // 출석 서비스 호출 및 출석 내역 반환 (DTO) - 몇월 몇주차? 월, 화, 수, 목, 금, 토, 일 (LocalDate, true/false)

        // 인기 검색어

        //
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
     *
     * @param member
     */
    @GetMapping("api/main/{id}/search/recent")
    public void getRecentSearch(
            @PathVariable("id") Member member
    ){

    }

}
