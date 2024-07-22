package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.domain.character.CharacterDetail;
import dismas.com.avocado.domain.character.MemberCharacter;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.character.CharacterDetailRepository;
import dismas.com.avocado.repository.character.CharacterRepository;
import dismas.com.avocado.repository.character.MemberCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterDetailRepository characterDetailRepository;
    private final MemberCharacterRepository memberCharacterRepository;

    @Transactional
    //기초 캐릭터 세팅(아보카도가 id 1로 저장돼어있다고 가정)
    public void setDefaultCharacter(Member member) {
        // Character를 ID 1로 조회
        Optional<Character> optionalCharacter = characterRepository.findById(1L);

        if (optionalCharacter.isPresent()) {
            Character character = optionalCharacter.get();

            MemberCharacter memberCharacter = MemberCharacter.builder()
                    .character(character)
                    .member(member)
                    .currentPoint(0L)
                    .nickname("일단 내맘대로 닉네임")
                    .isSelected(true)
                    .currentLevel(1L)
                    .build();

            // MemberCharacter 저장
            memberCharacterRepository.save(memberCharacter);
        } else {
            throw new IllegalArgumentException("Character with ID 1 does not exist.");
        }
    }

    /**
     * Member Character Exp Up Service
     * 캐릭터 경험치 증가 서비스
     * - 사용자가 보유한 캐릭터의 경험치 상승
     *      - 특정 기준 초과 시 레벨업 메서드 호출
     * @param member 사용자 ID
     * @param point  증가하는 포인트
     * @return boolean 레벨업 여부
     */
    @Transactional
    public boolean expUpCharacter(Member member, Long point) {
        // 선택된 캐릭터 가져오기
        MemberCharacter memberCharacter = memberCharacterRepository.findMemberCharacter(member)
                .orElseThrow(()-> new IllegalStateException("선택된 캐릭터가 없습니다."));

        // 포인트 업데이트
        memberCharacter.increaseCurrentPoint(point);

        Long currentPoint = memberCharacter.getCurrentPoint();
        Long nextLevel = memberCharacter.getCurrentLevel() + 1;
        Character character = memberCharacter.getCharacter();


        Optional<CharacterDetail> characterDetail = characterDetailRepository.findByLevel(nextLevel, character);

        boolean isLevelUp = characterDetail
                .map(detail -> currentPoint >= detail.getRequiredPoint())
                .orElse(false);

        if(isLevelUp){
            memberCharacter.levelUp();
        }

        memberCharacterRepository.save(memberCharacter);

        return isLevelUp;
    }

    /**
     * Get Character Image Service (캐릭터 이미지 조회 서비스)
     *
     */
    public String getCharacterImage(Member member){
        MemberCharacter memberCharacter = memberCharacterRepository.findMemberCharacter(member)
                .orElseThrow(()-> new IllegalStateException("선택된 캐릭터가 없습니다."));

        Long currentLevel = memberCharacter.getCurrentLevel();
        Character character = memberCharacter.getCharacter();

        return characterDetailRepository.findByLevel(currentLevel,character).get()
                .getImageUrl();
    }
}

