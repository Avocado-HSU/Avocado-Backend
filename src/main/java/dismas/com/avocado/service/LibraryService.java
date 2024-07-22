package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.LibraryWordDto;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseDto;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseType;
import dismas.com.avocado.mapper.LibraryMapper;
import dismas.com.avocado.repository.word.MemberWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseType.*;

/**
 * 라이브러리 서비스
 * - 등록
 * - 삭제
 * - 조회
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LibraryService {

    private final MemberWordRepository memberWordRepository;
    private final LibraryMapper libraryMapper;

    /**
     * Register Library Word Service (라이브러리 단어 등록/삭제 서비스)
     *
     * @param libraryId 라이브러리 ID
     */
    @Transactional
    public UpdateLibraryResponseDto updateLibrary(Long libraryId){
        // 라이브러리 단어의 최대 갯수 20개 - 20개 넘으면 추가 X, 클라이언트에 전달하는 로직 필요
        Optional<MemberWord> optionalMemberWord = memberWordRepository.findById(libraryId);

        if (optionalMemberWord.isEmpty()) {
            throw new RuntimeException("단어가 존재하지 않습니다.");
        }

        MemberWord memberWord = optionalMemberWord.get();
        UpdateLibraryResponseType responseType = ERROR;

        if (memberWord.getIsLibraryWord()) {
            // 라이브러리 단어 삭제
            memberWord.unregisterLibraryWord();
            responseType = DELETED;
        }else{
            // 라이브러리 단어 추가
            memberWord.registerLibraryWord();

            responseType = REGISTERED;
        }
        memberWordRepository.save(memberWord);

        return libraryMapper.toUpdateLibraryResponseDto(responseType, libraryId);
    }


    /**
     * Search Library Word Service (라이브러리 단어 조회 서비스)
     * 사용자의 라이브러리 단어를 조회 후 반환
     *
     * @param member 라이브러리 단어를 조회하기 위한 사용자 엔티티
     * @return List<LibraryDto> LibraryPageDto를 구성하기 위한 Dto 반환
     */
    public List<LibraryWordDto> getLibrary(Member member){
        List<MemberWord> memberWordList = memberWordRepository.findLibraryByMember(member, PageRequest.of(0, 20)).getContent();
        return libraryMapper.toLibraryDtos(memberWordList);
    }
}
