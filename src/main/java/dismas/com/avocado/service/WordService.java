package dismas.com.avocado.service;

import dismas.com.avocado.dto.parsingPage.WordMultiDto;
import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.domain.word.Word;
import dismas.com.avocado.dto.mainPage.RecommendWordDto;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import dismas.com.avocado.mapper.MainPageMapper;
import dismas.com.avocado.mapper.SearchMapper;
import dismas.com.avocado.repository.word.MemberWordRepository;
import dismas.com.avocado.repository.word.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 단어 관련 서비스
 *
 * @since 2024-07-10
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final MemberWordRepository memberWordRepository;

    private final OpenAiService openAiService;

    private final SearchMapper searchMapper;
    private final MainPageMapper mainPageMapper;
    /**
     * Word Search Service with GPT (생성형 AI 단어 검색 서비스)
     *
     * @param member 사용자 ID
     * @param word 검색하고자 하는 word
     */
    @Transactional
    public WordMultiDto searchWord(Member member, String word){
        return openAiService.handleSearchRequest(word);
        // 파싱 로직 추가 필요
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

    @Transactional
    public MemberWord insertMemberWord(
            Member member, String english, String etymology, String korean, String suffix, String prefix
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
                            .suffix(suffix)
                            .prefix(prefix)
                            .searchCount(1L)
                            .build()
            );
        }else{
            // 검색한 Word가 DB에 저장되어 있는 경우
            word = findWord.get();
            word.plusWordSearchCount();
        }

        Optional<MemberWord> findMemberWord = memberWordRepository.findByMemberAndWord(word, member);
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
        return memberWord;
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

    /**
     * Get Recommend Word Service (추천 단어 조회 서비스)
     *
     * 가장 조회가 많이 된 상위 30개 단어 중 랜덤으로 5개 단어를 선별해서 반환한다.
     *
     */
    public RecommendWordDto getRecommendWord(){
        List<Word> top30Word = wordRepository.findPopularWord(PageRequest.of(0, 30));

        Collections.shuffle(top30Word);

        List<Word> random5Words = top30Word.stream()
                .limit(5)
                .toList();

        return mainPageMapper.toRecommendWordDto(random5Words);
    }


}