package dismas.com.avocado.oauth2;

import dismas.com.avocado.dto.OAuthDto.CustomOAuth2User;
import dismas.com.avocado.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        // *주의* 되도록 setMaxAge랑 토큰의 수명주기가 같도록 해주어야합니다.
        // 만료 기간 하루 24 * 60 * 60 * 1000L
        String token = jwtUtil.createJwt(username, role, 24 * 60 * 60 * 1000L);

        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:3000/");
    }

    //쿠키 방식으로 진행
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        //24시간
        //*주의* jwtUtil.createJwt의 expiredMs와 같게 해주어야합니다.
        cookie.setMaxAge(24 * 60 * 60 * 1000);
        //https 사용 시 주석 해제
             //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
