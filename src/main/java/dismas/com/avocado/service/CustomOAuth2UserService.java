package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.OAuthDto.CustomOAuth2User;
import dismas.com.avocado.dto.OAuthDto.KakaoResponse;
import dismas.com.avocado.dto.OAuthDto.NaverResponse;
import dismas.com.avocado.dto.OAuthDto.OAuth2Response;
import dismas.com.avocado.dto.UserDTO;
import dismas.com.avocado.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final CharacterService characterService;
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Member existData = memberRepository.findByUsername(username);

        // 간편 로그인을 통한 최초 접근시(회원가입과 기능 유사)
        if(existData == null) {
            Member member=new Member();
            member.setUsername(username);
            member.setEmail(oAuth2Response.getEmail());
            member.setName(oAuth2Response.getName());
            member.setRole("ROLE_USER");
            member.setPoint(0L);
            memberRepository.save(member);
            // 초기 회원가입 시에 기본 캐릭터를 배정받는다.
            // 새로운 회원에게 기본 캐릭터 할당
            characterService.setDefaultCharacter(member);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");


            return new CustomOAuth2User(userDTO);
        }
        else { //유저 정보에 대한 소셜 로그인쪽 데이터 변동이 있을 경우 대비
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            memberRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}
