package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.domain.word.Word;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.word.MemberWordRepository;
import dismas.com.avocado.repository.word.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * Word Search Service (단어 검색 서비스)
     *
     * @param member 사용자 ID
     * @param word 검색하고자 하는 word
     */
    public void searchWord(Member member, String word){
        // 1. 단어 검증
        // 2. 단어 API 호출 및 검증
        // 3. GPT 모델 호출
        // 4. 반환값 파싱
        // 5. Word Service 호출
        // a. 기존에 단어가 있는지 확인 -> 없을 경우 생성
        // b. 해당 단어 기반으로 MemberWord 생성

        // 6. Dto 구성 및 반환 (MemberWord ID 포함)
    }

    /**
     * Delete Library Word Service (라이브러리 단어 삭제 서비스)
     *
     * @param member 사용자 ID
     * @param libraryWordId 라이브러리 단어 ID
     */
    public void deleteLibraryWord(Member member, Long libraryWordId){

    }

    /**
     * create Library Word Service (라이브러리 단어 추가 서비스)
     * - 단어 테이블에 해당 단어가 없으면 추가
     * - 사용자와 단어간의 관계 구성 (MemberWord)
     *
     * @param member 사용자 ID
     * @param english 영어 단어
     * @param etymology 어원
     * @param korean 영어 단어 한글 해석
     * @param audioUrl 영어 단어 음성 파일
     */
    public Long createLibraryWord(
            Member member, String english, String etymology, String korean, String audioUrl
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
                            .audioUrl(audioUrl)
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
    public void validationWord(String word){

    }


}
