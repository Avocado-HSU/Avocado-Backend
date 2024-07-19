package dismas.com.avocado.sevice;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.domain.character.CharacterDetail;
import dismas.com.avocado.dto.CharacterDTO;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.character.CharacterDetailRepository;
import dismas.com.avocado.repository.character.CharacterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository customCharacterRepository;
    private final CharacterDetailRepository characterDetailRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public boolean purchaseCharacter(Member member, String characterDetailName) {
      //  Member member = findMemberByMemberId(memberId);

        CharacterDetail characterDetail = characterDetailRepository.findFirstByNameOrderByLevelAsc(characterDetailName);

        // 가격이 0이 아니고 유저의 포인트가 충분한지 확인
        if (characterDetail.getPrice() > 0 && member.getPoint() >= characterDetail.getPrice()) {
            member.minusMemberPoint(characterDetail.getPrice());

            Character customCharacter = Character.builder()
                    .price(characterDetail.getPrice())
                    .currentPoint(0L)
                    .characterDetail(characterDetail)
                    .member(member)
                    .build();



            customCharacterRepository.save(customCharacter);
            member.getCustomCharacters().add(customCharacter);
            memberRepository.save(member);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Character levelUpCharacter(Long characterId, Long extraPoints) {
        Character character = customCharacterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터 ID입니다: " + characterId));

        Long currentLevel = character.getCharacterDetail().getLevel();
        String characterName = character.getCharacterDetail().getName();
        CharacterDetail nextLevelDetail = characterDetailRepository.findByLevelAndName(currentLevel + 1, characterName);

        if (nextLevelDetail == null) {
           Long tmpRequiredPoint=character.getCharacterDetail().getRequiredPoint();
           character.setCurrentPoint(tmpRequiredPoint);
           customCharacterRepository.save(character);
           return character;
        }

        // 레벨업 처리
        character.setCharacterDetail(nextLevelDetail);
        // 포인트는 다음 레벨에서의 포인트 요구사항에 맞게 설정합니다.
        character.setCurrentPoint(extraPoints);

        customCharacterRepository.save(character);

        return character;
    }

    @Transactional
    public Character expUpCharacter(Long characterId, Long increasePoint) {
        Character character = customCharacterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터 ID입니다: " + characterId));

        Long currentPoint = character.getCurrentPoint() == null ? 0L : character.getCurrentPoint();
        Long requiredPoints = character.getCharacterDetail().getRequiredPoint();

        Long newPoint = currentPoint + increasePoint;

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
            customCharacterRepository.save(character);
            return character;
        }
    }


    @Transactional
    public List<CharacterDTO> getAllCharactersByMemberId(Member member) {
        //Member member = findMemberByMemberId(memberId);

        List<Character> characters = member.getCustomCharacters();
        return characters.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CharacterDTO mapToDTO(Character character) {
        return new CharacterDTO(
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
}

