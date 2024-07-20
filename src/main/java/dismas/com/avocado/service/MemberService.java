package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final CharacterService characterService;

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

        member.plusMemberPoint(point);
        characterService.expUpCharacter(member, point);

    }


}
