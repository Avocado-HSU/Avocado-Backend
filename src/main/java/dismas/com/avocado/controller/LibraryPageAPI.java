package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.libraryPage.LibraryDeleteResponseDto;
import dismas.com.avocado.dto.libraryPage.LibraryPageResponseDto;
import dismas.com.avocado.dto.libraryPage.LibraryResponseDto;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import dismas.com.avocado.mapper.LibraryMapper;
import dismas.com.avocado.mapper.WordPageMapper;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.LibraryService;
import dismas.com.avocado.service.OpenAiService;
import dismas.com.avocado.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LibraryPageAPI {

    private final LibraryMapper libraryMapper;
    private final WordPageMapper wordPageMapper;

    private final CharacterService characterService;
    private final LibraryService libraryService;
    private final WordService wordService;
    private final OpenAiService openAiService;

    /**
     * Library Page API
     *  1. 캐릭터 이미지 반환
     *  2. 라이브러리 단어 반환
     *      - LibraryResponseDto의 size가 0일 시, client에서 별도의 안내가 필요 (라이브러리 단어를 지정해 보세요!)
     * @param member 사용자 ID
     * @return LibraryPageResponseDto 라이브러리 페이지를 구성하기 위한 DTO 반환
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/library/{id}")
    public LibraryPageResponseDto getLibraryPage(@PathVariable("id") Member member){

        List<LibraryResponseDto> libraryResponseDtos = libraryService.getLibrary(member);

        // 추후 캐릭터 이미지 가져오는 로직 추가
        String imgUrl = characterService.getCharacterImage(member);

        return libraryMapper.toLibraryPageDto(libraryResponseDtos, imgUrl);
    }

    /**
     * Delete Library Word API (라이브러리 단어 삭제 API)
     *
     * @param member 사용자 ID
     * @param libraryId 라이브러리 단어 ID (서버에서 Library Page 요청 시 제공)
     *
     */
    @DeleteMapping("api/library/{id}/{libraryId}")
    public ResponseEntity<LibraryDeleteResponseDto> deleteLibraryWord(
            @PathVariable("id") Member member,
            @PathVariable("libraryId") Long libraryId){

        boolean isDeleted = libraryService.deleteLibrary(libraryId);
        LibraryDeleteResponseDto responseDto = libraryMapper.toLibraryDeleteResponseDto(isDeleted);

        if (isDeleted) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
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
    public ResponseEntity<SearchWordResponseDto> searchWord(
            @PathVariable("id") Member member,
            @PathVariable("word") String word){

        if(wordService.validateWord(word)){
            try {
                String characterImgUrl = characterService.getCharacterImage(member);
                Map<SearchRequestType, String> contents = openAiService.handleSearchRequest(word);
                return ResponseEntity.ok(wordPageMapper.toSearchWordResponseDto(true, characterImgUrl, contents));
            }catch (RuntimeException e){
                // OpenAI 에러
                return ResponseEntity.internalServerError().body(wordPageMapper.toSearchWordResponseDto(false, null, null));
            }
        }else{
            // 단어 검증 실패
            return ResponseEntity.badRequest().body(wordPageMapper.toSearchWordResponseDto(false, null, null));
        }

    }

}
