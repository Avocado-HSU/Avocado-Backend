package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.domain.character.CharacterDetail;
import dismas.com.avocado.dto.CharacterDto;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.character.CharacterDetailRepository;
import dismas.com.avocado.repository.character.CharacterRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterDetailRepository characterDetailRepository;
    private final MemberRepository memberRepository;


    /**
     * Member Character Exp Up Service
     * 캐릭터 경험치 증가 서비스
     * - 사용자가 보유한 캐릭터의 경험치 상승
     *      - 특정 기준 초과 시 레벨업 메서드 호출
     * @param characterId
     * @param point
     * @return
     */
    @Transactional
    public Character expUpCharacter(Member member, Long point) {

        // 지금 사용자가 선택한 캐릭터?
        // 캐릭터 -> isSelected = true
        // 캐릭터(point bukkit) <-> 캐릭터 디테일 (Lv2 100, Lv3 200, Lv4 300, Lv5 400)
        // 캐릭터(point 올려) -> 현재 내 캐릭터 포인트는 87 -> 102로 올랐어
            // 캐릭터 디테일 가지고 있는 것중 가장 레벨이 높은거 가져와
            // 그거랑 비교해 100 < 102
            // 캐릭터 디테일 Lv2 생성 후 캐릭터에 연결
                // 캐릭터 이미지 -> 가지고 있는 캐릭터 디테일 중 가장 레베루가 높은걸로



        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터 ID입니다: " + characterId));

        Long currentPoint = character.getCurrentPoint() == null ? 0L : character.getCurrentPoint();
        Long requiredPoints = character.getCharacterDetail().getRequiredPoint();

        Long newPoint = currentPoint + point;

        // 요구 경험치 충족 여부를 확인하여 레벨업 처리
        if( newPoint >= requiredPoints ) {
            Long extraPoints = newPoint - requiredPoints;

            // 레벨업을 수행하고 남은 포인트를 설정
            character = levelUpCharacter(characterId, extraPoints);
            return character;
        }
        else {
            // 최종 포인트 설정
            character.setCurrentPoint(newPoint);
            characterRepository.save(character);
            return character;
        }
    }

    @Transactional
    public Character levelUpCharacter(Long characterId, Long extraPoints) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터 ID입니다: " + characterId));

        Long currentLevel = character.getCharacterDetail().getLevel();
        String characterName = character.getCharacterDetail().getName();
        CharacterDetail nextLevelDetail = characterDetailRepository.findByLevelAndName(currentLevel + 1, characterName);

        if (nextLevelDetail == null) {
            Long tmpRequiredPoint=character.getCharacterDetail().getRequiredPoint();
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



    public List<CharacterDto> getAllCharactersByMemberId(Member member) {
        List<Character> characters = characterRepository.findAll();

        return characters.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CharacterDto mapToDTO(Character character) {
        return new CharacterDto(
                character.getId(),
                character.getCharacterDetail().getName(),
                character.getPrice(),
                character.getCharacterDetail().getLevel(),
                character.getCharacterDetail().getPrefix(),
                character.getCharacterDetail().getDescription(),
                character.getCharacterDetail().getImageUrl(),
                character.getCurrentPoint(),
                character.getCharacterDetail().getRequiredPoint()
        );
    }


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
}