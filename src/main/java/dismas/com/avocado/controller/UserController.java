package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Hidden
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final MemberRepository memberRepository;

    @Operation(summary = "현재 로그인 중인 유저의 정보", description = "현재 로그인 중인 유저의 정보를 ResponseEntity<Member> 형태로 받아옵니다."
            , tags = { "Member Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/user/me")
    public ResponseEntity<Member> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {

        String username = principal.getName();
        System.out.println("뭘까요?: " + username);
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(member);
    }

}
