package dismas.com.avocado.jwt;

import dismas.com.avocado.dto.CustomOAuth2User;
import dismas.com.avocado.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isExcludedPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        //쿠키들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾는다.
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {

                authorization = cookie.getValue();
            }
        }

        //Authorization Header 검증
        if (authorization == null) {

            System.out.println("토큰이 비어있습니다.");
            filterChain.doFilter(request, response);
            //response.sendRedirect("http://localhost:3000/");

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

        //토큰 소멸 시간 검증 분기점
        if (jwtUtil.isExpired(token)) {

            System.out.println("token 기간 소멸");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 -> 필수적인 요소
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
    private boolean isExcludedPath(String requestURI) {
        return pathMatcher.match("/", requestURI)
                || pathMatcher.match("/swagger-ui/index.html", requestURI)
                || pathMatcher.match("/swagger-ui/**", requestURI)
                || pathMatcher.match("/v3/api-docs/**", requestURI)
                || pathMatcher.match("/health", requestURI);
    }

}