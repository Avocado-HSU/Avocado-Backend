package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.service.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordPageAPI {
    private final WordService wordService;

    public WordPageAPI(WordService wordService) {
        this.wordService = wordService;
    }

    // WordPage 는 각 페이지 Search API를 통해 넘어온다.



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
    public void wordSearch(
            @PathVariable("id") Member member,
            @PathVariable("word") String word){

        // 검증 서비스 호출

        // 검색 서비스 호출 -> Dto 생성
        // Word 서비스 호출 (Member Word 생성)

        // Dto 반환
    }

}
