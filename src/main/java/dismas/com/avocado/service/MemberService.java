package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final CharacterService characterService;

    /**
     * Update Member Point Service (사용자 포인트 증가 서비스)
     * - 사용자 포인트 증가
     * - 사용자 보유 캐릭터 포인트 증가
     *
     * @param member 사용자 ID
     * @param point 증가 or 감소하는 포인트의 총량
     */
    @Transactional
    public void updatePoint(Member member, Long point){
        // 사용자 포인트 증가
        member.plusMemberPoint(point);
        // 사용자 보유 캐릭터 포인트 증가
        characterService.expUpCharacter(member, point);

        // 레벨업 여부 반환 체크
    }
}
