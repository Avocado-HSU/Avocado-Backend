package dismas.com.avocado.service;


import dismas.com.avocado.domain.Member;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.word.MemberWordRepository;
import dismas.com.avocado.repository.word.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 검색 서비스
 * 검색 및 검색 요청 API 기능 구현
 *
 * @since 2024-07-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;
    private final MemberWordRepository memberWordRepository;


    public void wordSearch(Member member, String word){



    }


}
