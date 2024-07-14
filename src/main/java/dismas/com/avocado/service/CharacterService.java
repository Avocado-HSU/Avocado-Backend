package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.repository.character.CharacterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;

    /**
     * Get Member Character Service (사용자 캐릭터 조회 서비스)
     *
     */
    public void searchCharacter(Member member){

    }

}
