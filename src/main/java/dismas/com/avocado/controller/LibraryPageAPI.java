package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryPageAPI {

    /**
     * Library Page API
     * - 라이브러리로 지정한 값 중
     * - 출석 관련 데이터 반환 (n월 m주차, 몇요일 출석)
     * - 인기 검색어 (최대 5개 반환)
     * - 추천 단어 반환
     * @param member 사용자 id
     */
    @GetMapping("api/library/{id}")
    public void getLibraryPage(@PathVariable("id") Member member){

    }

    /**
     * Delete Library Word API (라이브러리 단어 삭제 API)
     *
     * @param member 사용자 ID
     * @param libraryId 라이브러리 단어 ID (서버에서 Library Page 요청 시 제공)
     */
    @DeleteMapping("api/library/{id}/{libraryId}")
    public void deleteLibraryWord(
            @PathVariable("id") Member member,
            @PathVariable("libraryId") Long libraryId){

    }

    /**
     * Library Page Search API (라이브러리 검색 API)
     * - 라이브러리 페이지에서 검색창 이용시 호출
     * - 라이브러리 단어 검색 시 호출
     *
     * @param member 사용자 ID
     * @param word 라이브러리 단어 ID (서버에서 Library Page 요청 시 제공)
     */
    @GetMapping("api/library/{id}/search/{word}")
    public void searchWord(
            @PathVariable("id") Member member,
            @PathVariable("word") String word){

    }

}
