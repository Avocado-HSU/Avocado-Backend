package dismas.com.avocado.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    //DB에 저장하게 될 유저 정보 추후 확장 가능
    public String role;
    public String name;
    public String username;
}
