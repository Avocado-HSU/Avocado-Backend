package dismas.com.avocado.service;

import dismas.com.avocado.domain.word.PopularWord;
import dismas.com.avocado.domain.word.Word;
import dismas.com.avocado.dto.mainPage.PopularWordDto;
import dismas.com.avocado.mapper.MainPageMapper;
import dismas.com.avocado.repository.word.PopularWordRepository;
import dismas.com.avocado.repository.word.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 인기 검색어 서비스
 * - 조회 - 등록 - 삭제 (cron)
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PopularWordService {

    private final WordRepository wordRepository;
    private final PopularWordRepository popularWordRepository;

    private final MainPageMapper mainPageMapper;

    /**
     * Update Popular Word Service (인기 검색어 업데이트 서비스)
     * Cron을 이용하여 매일 오전에 인기 검색어를 업데이트 한다
     */
    @Transactional
    @Scheduled(cron = "0 0 0/3 * * *", zone = "Asia/Seoul")
    public void updatePopularWord(){
        deletePopularWord();
        savePopularWord();
    }

    @Transactional
    public void savePopularWord(){
        List<Word> popularWords =  wordRepository.findPopularWord(PageRequest.of(0, 5));
        popularWords.stream()
                .map(word -> PopularWord.builder()
                        .English(word.getEnglish())
                        .build())
                .forEach(popularWordRepository::save);
    }

    /**
     * Delete Popular Word Service (인기 검색어 삭제 서비스)
     * 인기 검색어 업데이트 전 삭제한다.
     */
    @Transactional
    public void deletePopularWord(){
        wordRepository.deleteAll();
    }

    /**
     * Get Popular Word Service (인기 검색어 조회 서비스)
     * 메인 페이지에서 조회할 인기 검색어를 찾는다.
     */
    public PopularWordDto getPopularWord(){
        return mainPageMapper.toPopularWordDto(popularWordRepository.findPopularWord(PageRequest.of(0, 5)));
    }


}
