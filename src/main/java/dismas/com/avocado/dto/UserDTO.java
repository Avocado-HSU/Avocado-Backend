package dismas.com.avocado.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    //DB에 저장하게 될 유저 정보 추후 확장 가능
    private String role;
    private String name;
    private String username;
}
