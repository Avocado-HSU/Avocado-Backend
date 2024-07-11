package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordPageAPI {

    // WordPage 는 각 페이지 Search API를 통해 넘어온다.

    /**
     * 라이브러리 등록 API
     *
     * - 기존에 라이브러리에 등록되어 있는지 확인
     * - 등록 되어 있다면 -> setLibraryResponseDto : true
     * - 등록 되지 않았다면 -> setLibraryResponseDto : false
     *
     * @param member 사용자 Entity
     * @param word 사용자가 입력한 검색 값
     */
    @GetMapping("api/word/{id}/library/{word}")
    public void getWordPage(
            @PathVariable("id") Member member,
            @PathVariable("word") String word){

        // word 에 대한 검증 서비스 호출 (비즈니스)

        // Library 서비스 호출 (등록 서비스)
            // Library 서비스 저장이 20개가 넘었는지 검증 체크

        // Dto 생성 및 반환
    }

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
