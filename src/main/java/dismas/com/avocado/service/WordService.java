package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.domain.word.Word;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import dismas.com.avocado.mapper.SearchMapper;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.word.MemberWordRepository;
import dismas.com.avocado.repository.word.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 단어 관련 서비스
 *
 * @since 2024-07-10
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;
    private final MemberWordRepository memberWordRepository;

    private final SearchMapper searchMapper;

    /**
     * Word Search Service with GPT (생성형 AI 단어 검색 서비스)
     *
     * @param member 사용자 ID
     * @param word 검색하고자 하는 word
     */
    public void searchWord(Member member, String word){

        // 1. 단어 검증
        if(validateWord(word)){
            // 2. GPT 모델 호출
            // 4. Word Service 호출
                // createLibraryWord(Member member, String english, String etymology, String korean, String audioUrl);
            // 6. Dto 구성 및 반환 (MemberWord ID 포함)
                // a. 캐릭터 반환 (characterService 호출)
                // b. 단어 반환
                // c. 단어의 의미 (장문)
                // d. 어원 분리 (상단 파싱)
                // e. 예문 (예문과 뜻)
                // f. 단어를 쉽게 외우는 팁
                // g. 유사 단어 5개
                // i. 같은 접두사를 가진 단어 5개
        }else{
            // false DTO 반환
        }
    }

    /**
     * create Word Service (사용자 단어 엔티티 추가 서비스)
     * - 사용자와 단어간의 관계 구성 (MemberWord)
     * - 라이브러리 단어는 별도 설정 필요
     *
     * @param member 사용자 ID
     * @param english 영어 단어
     * @param etymology 어원
     * @param korean 영어 단어 한글 해석
     */
    public Long insertMemberWord(
            Member member, String english, String etymology, String korean
    ) {
        Optional<Word> findWord = wordRepository.findWordByString(english);
        Word word;
        if(findWord.isEmpty()){
           // 검색한 Word가 DB에 저장되어 있지 않은 경우
            word = wordRepository.save(
                    Word.builder()
                            .english(english)
                            .etymology(etymology)
                            .korean(korean)
                            .searchCount(1L)
                            .build()
            );
        }else{
            // 검색한 Word가 DB에 저장되어 있는 경우
            word = findWord.get();
            word.plusWordSearchCount();
        }

        Optional<MemberWord> findMemberWord = memberWordRepository.findByWord(word);
        MemberWord memberWord;
        if(findMemberWord.isEmpty()){
            // 해당 Word가 Library에 등록되어 있지 않을 경우
            memberWord = memberWordRepository.save(
                    MemberWord.builder()
                            .word(word)
                            .member(member)
                            .searchCount(1L)
                            .isLibraryWord(false)
                            .build()
            );
        }else{
            // 해당 Word가 Library에 등록되어 있을 경우
           memberWord = findMemberWord.get();
           memberWord.plusMemberWordSearchCount();
        }
        return memberWord.getId();
    }

    /**
     * Validate Input Word Service (입력된 단어 검증 서비스)
     * 사용자가 입력한 단어가 실제 입력 가능한지 검증하는 서비스 (비즈니스)
     *
     * @param word 검증하고자 하는 단어
     */
    public boolean validateWord(String word){

        if(word.contains(" ")){
            log.debug("입력된 단어 {}에 공백이 있습니다", word);
            return false;
        }


        if(isNotAlphabetical(word)){
            log.debug("입력된 단어 {}는 알파벳으로 구성되지 않았습니다", word);
            return false;
        }


        word = word.trim();
        if(word.isEmpty()){
            return false;
        }

        //openFeign(FreeDictionary을 이용하여 마지막 검증 수행
            //404 에러 발생 시 예외 처리 수행
            //505 에러 발생 시 예외 처리 수행

        return true;
    }

    public boolean isNotAlphabetical(String word){
        return !Pattern.matches("^[a-zA-Z]+$", word);
    }

    /**
     * Get Recent Search Word Service (최근 검색 단어 조회 서비스)
     * 최근 검색된 단어를 조회하는 서비스
     * @param member 사용자
     * @return RecentSearchWordResponseDto 최근 검색 단어 반환 DTO
     */
    public RecentSearchWordResponseDto getRecentSearchWord(Member member){
        return searchMapper.toRecentSearchWordResponseDto(memberWordRepository.findByMember(member, PageRequest.of(0, 10)).getContent());
    }


}
