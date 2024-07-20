package dismas.com.avocado.controller.Test;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super-user")
public class SuperController {

    @Autowired
    MemberRepository memberRepository;

    @Operation(summary = "편의성을 위한 회원 제거 슈퍼 유저 권한", description = "회원 정보가 삭제 및 해당 회원과" +
            "연관된 캐릭터가 삭제됩니다..", tags = { "Member Controller" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/delete-user")
    public void deleteUser(
            @Parameter(description = "현재 로그인 중인 회원 삭제",required = true)
            @AuthenticationPrincipal OAuth2User deleteCurrentUser){
        String username = deleteCurrentUser.getName();
        System.out.println(username);
        Member member = memberRepository.findByUsername(username);
        memberRepository.delete(member);
    }
}
