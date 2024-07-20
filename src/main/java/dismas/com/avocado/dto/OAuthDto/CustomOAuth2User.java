package dismas.com.avocado.dto.OAuthDto;

import dismas.com.avocado.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
//프론트 엔드에게 정보를 전달하기 위해서 필수적인 부분
public class CustomOAuth2User implements OAuth2User {
    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole();
            }
        });

        return collection;
    }




    // * 중요 *
    @Override
    public String getName() {
        return userDTO.getUsername();
    }


    public String getUsername() {

        return userDTO.getUsername();
    }

}
