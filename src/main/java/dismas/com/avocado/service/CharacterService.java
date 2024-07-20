package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.domain.character.CharacterDetail;
import dismas.com.avocado.domain.character.MemberCharacter;
import dismas.com.avocado.dto.CharacterDto;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.character.CharacterDetailRepository;
import dismas.com.avocado.repository.character.CharacterRepository;

import dismas.com.avocado.repository.character.MemeberCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterDetailRepository characterDetailRepository;
    private final MemberRepository memberRepository;
    private final MemeberCharacterRepository memberCharacterRepository;

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
     * @param
     * @param point
     * @return
     */
    @Transactional
    public CharacterDto expUpCharacter(Long memberId, Long point) {
        Optional<Member> getMember = memberRepository.findById(memberId);
        Member member = getMember.get();
        // 선택된 캐릭터 가져오기
        MemberCharacter userSelectedCharacter = memberCharacterRepository.findByMemberIdAndIsSelectedTrue(memberId);

        if (userSelectedCharacter == null) {
            throw new IllegalStateException("선택된 캐릭터가 없습니다.");
        }

        Character character = userSelectedCharacter.getCharacter();
        // 포인트 업데이트
        userSelectedCharacter.increaseCurrentPoint(point);
        memberCharacterRepository.save(userSelectedCharacter); // 데이터베이스에 변경 사항 저장

        Long newPoint = userSelectedCharacter.getCurrentPoint();
        // 캐릭터의 모든 디테일을 레벨 순서로 가져오기
        List<CharacterDetail> details = characterDetailRepository.findByCharacterIdOrderByLevelAsc(character.getId());

        CharacterDetail currentDetail = null;
        // 일단 초기 값을 max레벨 달성시의 캐릭정보로 둔다...
        CharacterDetail nextDetail = details.get(details.size()-1);

        // 현재 및 다음 레벨의 디테일을 찾기 위한 루프
        for (int i = 0; i < details.size(); i++) {
            CharacterDetail detail = details.get(i);

            if (newPoint >= detail.getRequiredPoint()) {
                currentDetail = detail; // 현재 포인트가 요구 포인트보다 크거나 같으면 현재 레벨 업데이트
                // 다음 레벨 디테일이 있는 경우
                if (i < details.size() - 1) {
                    nextDetail = details.get(i + 1);
                }
            } else {
                break; // 더 이상 레벨업이 불가능한 상태
            }
        }
        // CharacterDto 생성
        CharacterDto characterDto = CharacterDto.builder()
                .level(currentDetail.getLevel())
                .currentPoint(userSelectedCharacter.getCurrentPoint())
                .name(character.getName())
                .description(currentDetail.getDescription())
                .imageUrl(currentDetail.getImageUrl())
                .prefix(currentDetail.getPrefix())
                .requiredPoint(nextDetail.getRequiredPoint())
                .build();

        return characterDto;
    }
}

/*
    @Transactional
    public Character levelUpCharacter(Long characterId, Long extraPoints) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터 ID입니다: " + characterId));

        Long currentLevel = character.getCharacterDetail().getLevel();
        String characterName = character.getCharacterDetail().getName();
        CharacterDetail nextLevelDetail = characterDetailRepository.findByLevelAndName(currentLevel + 1, characterName);

        if (nextLevelDetail == null) {
            Long tmpRequiredPoint = character.getCharacterDetail().getRequiredPoint();
            character.setCurrentPoint(tmpRequiredPoint);
            characterRepository.save(character);
            return character;
        }

        // 레벨업 처리
        character.setCharacterDetail(nextLevelDetail);
        // 포인트는 다음 레벨에서의 포인트 요구사항에 맞게 설정합니다.
        character.setCurrentPoint(extraPoints);

        characterRepository.save(character);

        return character;
    }

*/

    //    @Transactional
//    public boolean purchaseCharacter(Member member, String characterDetailName) {
//
//        CharacterDetail characterDetail = characterDetailRepository.findFirstByNameOrderByLevelAsc(characterDetailName);
//
//        // 가격이 0이 아니고 유저의 포인트가 충분한지 확인
//        if (characterDetail.getPrice() > 0 && member.getPoint() >= characterDetail.getPrice()) {
//            member.minusMemberPoint(characterDetail.getPrice());
//
//            Character customCharacter = Character.builder()
//                    .price(characterDetail.getPrice())
//                    .currentPoint(0L)
//                    .characterDetail(characterDetail)
//                    .member(member)
//                    .build();
//
//            characterRepository.save(customCharacter);
//            member.getCustomCharacters().add(customCharacter);
//            memberRepository.save(member);
//
//            return true;
//        } else {
//            return false;
//        }
//    }
