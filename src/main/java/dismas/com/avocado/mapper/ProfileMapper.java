package dismas.com.avocado.mapper;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.dto.CharacterDto;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileMapper {


    public void toCharacterDto(List<Character> characters){
        characters.stream()
                .map(character -> CharacterDto.builder()
                        .id(character.getId())
                        .imageUrl(null)
                        .name(null)
                        .description(null)
                        .level(null)
                        .build())
                .toList();
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
}
