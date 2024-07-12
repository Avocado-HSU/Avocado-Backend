package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.MemberDTO;
import dismas.com.avocado.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@RequestMapping("/api")
public class UserController {

    private final MemberRepository memberRepository;

    public UserController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/user/me")
    public ResponseEntity<MemberDTO> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        // Extract username from OAuth2User
        String username = principal.getName(); // This should now be the username
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Convert to DTO and return
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setUsername(member.getUsername());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setRole(member.getRole());

        return ResponseEntity.ok(memberDTO);
    }

}
