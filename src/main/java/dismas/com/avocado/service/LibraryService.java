package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.LibraryResponseDto;
import dismas.com.avocado.mapper.LibraryMapper;
import dismas.com.avocado.repository.word.MemberWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
     * Register Library Word Service (라이브러리 단어 등록 서비스)
     *
     * @param memberWord 사용자 단어 (단어와 사용자의 관계 테이블)
     */
    @Transactional
    public void updateLibrary(MemberWord memberWord){
        // 라이브러리 단어의 최대 갯수 20개 - 20개 넘으면 추가 X, 클라이언트에 전달하는 로직 필요
        memberWord.registerLibraryWord();
    }

    /**
     * Delete Library Word Service (라이브러리 단어 삭제 서비스)
     *
     * @param id 삭제할 라이브러리 단어
     * @return 삭제 성공시 true, 그렇지 않으면 false
     */
    @Transactional
    public boolean deleteLibrary(Long id){
        return memberWordRepository.findById(id)
                .map(libraryWord ->{
                    libraryWord.unregisterLibraryWord();
                    return true;
                })
                .orElseGet(()-> {
                    log.error("라이브러리 단어를 찾을 수 없습니다. id = {}", id);
                    return false;
                });
    }

    /**
     * Search Library Word Service (라이브러리 단어 조회 서비스)
     * 사용자의 라이브러리 단어를 조회 후 반환
     *
     * @param member 라이브러리 단어를 조회하기 위한 사용자 엔티티
     * @return List<LibraryDto> LibraryPageDto를 구성하기 위한 Dto 반환
     */
    public List<LibraryResponseDto> getLibrary(Member member){
        List<MemberWord> memberWordList = memberWordRepository.findLibraryByMember(member, PageRequest.of(0, 20)).getContent();
        return libraryMapper.toLibraryDtos(memberWordList);
    }
}
