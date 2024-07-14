package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.libraryPage.LibraryDto;
import dismas.com.avocado.dto.libraryPage.LibraryPageDto;
import dismas.com.avocado.mapper.LibraryMapper;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LibraryPageAPI {

    private final LibraryMapper libraryMapper;

    private final LibraryService libraryService;
    private final CharacterService characterService;

    /**
     * Library Page API
     *  1. 캐릭터 이미지 반환
     *  2. 라이브러리 단어 반환
     * @param member 사용자 id
     */
    @GetMapping("api/library/{id}")
    public LibraryPageDto getLibraryPage(@PathVariable("id") Member member){

        List<LibraryDto> libraryDtos = libraryService.getLibrary(member);

        // 추후 캐릭터 이미지 가져오는 로직 추가
        String dummyUrl = "dummy";

        return libraryMapper.toLibraryPageDto(libraryDtos, dummyUrl);
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
        // 빈 라이브러리 참조 시 예외처리 로직 추후 추가할 것
        libraryService.deleteLibrary(libraryId);
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
