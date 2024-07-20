package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.dto.CharacterDto;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterService characterService;
    private final MemberRepository memberRepository;
/*
    @Operation(summary = "캐릭터 구매", description = "특정 캐릭터를 캐릭터 이름으로 구매합니다." +
            "true는 구매 성공이며 false는 구매 실패(포인트 부족 혹은 존재하지 않는 캐릭터입니다.)"
            , tags = { "Chracter Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/purchase-character")
    public ResponseEntity<Boolean> purchaseCharacter(
            @AuthenticationPrincipal OAuth2User principal,
            @Parameter(description = "구매할 캐릭터 이름",required = true,example = "사자")
            @RequestParam String characterName) {
        String username = principal.getName();
        System.out.println(username);
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(characterService.purchaseCharacter(member,characterName));
    }
*/



//    @Operation(summary = "유저의 포인트 up", description = "현재 유저의 포인트를 증가시켜줍니다(포인트는 캐릭터 구매시 소모)"
//            , tags = { "Chracter Controller" })
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
//            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
//            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
//    })
//    @PostMapping("/point-up")
//    public ResponseEntity<String> increaseMemberPoint(
//            @AuthenticationPrincipal OAuth2User principal,
//            @Parameter(description = "증가 시켜줄 포인트", required = true, example = "50")
//            @RequestParam Long increasePoint){
//
//        String username = principal.getName();
//        log.debug("userName = {}", username);
//        Member member = memberRepository.findByUsername(username);
//
//        member.plusMemberPoint(increasePoint);
//        memberRepository.save(member);
//        return ResponseEntity.ok("정상");
//    }


/*
    @Operation(summary = "유저가 보유중인 캐릭터 명단", description = "현재 유저가 보유 중인 캐릭터 명단을 보여줍니다."
            , tags = { "Chracter Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/find-user-characters")
    public ResponseEntity<List<CharacterDto>> getCharactersByMemberId(
            @AuthenticationPrincipal OAuth2User principal) {
        String username = principal.getName();
        System.out.println(username);
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 회원의 모든 캐릭터 조회
        List<CharacterDto> characters = characterService.getAllCharactersByMemberId(member);

        // 캐릭터가 없을 경우 NOT_FOUND 반환
        if (characters.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 캐릭터 리스트 반환
        return ResponseEntity.ok(characters);
    }

    */


    @Operation(summary = "유저가 보유중인 특정 캐릭터의 경험치 up", description = "보유중인 캐릭터의 경험치를 증가시켜줍니다."
            , tags = { "Chracter Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/exp-up")
    public ResponseEntity<CharacterDto> expUpCharacter(
            @Parameter(description = "해당 유저의 캐릭터 id",required = true,example = "2")
            @RequestParam Long memberId,
            @Parameter(description = "증가 시켜줄 포인트 양",required = true,example = "500")
            @RequestParam Long increasePoint) {
        CharacterDto customCharacter=characterService.expUpCharacter(memberId, increasePoint);

        return ResponseEntity.ok(customCharacter
        );
    }
}
