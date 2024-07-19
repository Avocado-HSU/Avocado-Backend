package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.dto.CharacterDTO;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.sevice.CharacterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterService characterService;
    private final MemberRepository memberRepository;
    public CharacterController(CharacterService characterService, MemberRepository memberRepository) {
        this.characterService = characterService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/purchase-character")
    public ResponseEntity<Boolean> purchaseCharacter(@AuthenticationPrincipal OAuth2User principal,@RequestParam String characterName) {
        // Extract username from OAuth2User
        String username = principal.getName();
        System.out.println(username);
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 회원의 모든 캐릭터 조회


        // 캐릭터 리스트 반환
        return ResponseEntity.ok(characterService.purchaseCharacter(member,characterName));
    }
    @PostMapping("/point-up")
    public ResponseEntity<String> increaseMemberPoint(@AuthenticationPrincipal OAuth2User principal,@RequestParam Long increasePoint){
        String username = principal.getName();
        System.out.println(username);
        Member member = memberRepository.findByUsername(username);

        member.plusMemberPoint(increasePoint);
        memberRepository.save(member);
        return ResponseEntity.ok("정상");
    }
    @GetMapping("/find-user-characters")
    public ResponseEntity<List<CharacterDTO>> getCharactersByMemberId(@AuthenticationPrincipal OAuth2User principal) {
        // Extract username from OAuth2User
        String username = principal.getName();
        System.out.println(username);
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 회원의 모든 캐릭터 조회
        List<CharacterDTO> characters = characterService.getAllCharactersByMemberId(member);

        // 캐릭터가 없을 경우 NOT_FOUND 반환
        if (characters.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 캐릭터 리스트 반환
        return ResponseEntity.ok(characters);
    }
    @PostMapping("/exp-up")
    public ResponseEntity<CharacterDTO> expUpCharacter(@RequestParam Long id, @RequestParam Long increasePoint) {
        Character customCharacter=characterService.expUpCharacter(id, increasePoint);

        return ResponseEntity.ok(new CharacterDTO(
                customCharacter.getId(),customCharacter.getCharacterDetail().getName(),customCharacter.getPrice(),
                customCharacter.getCharacterDetail().getLevel(),customCharacter.getCharacterDetail().getPrefix(),
                customCharacter.getCharacterDetail().getDescription(),customCharacter.getCharacterDetail().getImageUrl(),
                customCharacter.getCurrentPoint(),customCharacter.getCharacterDetail().getRequiredPoint()
        ));
    }
}
