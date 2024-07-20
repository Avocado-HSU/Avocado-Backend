package dismas.com.avocado.dto.OAuthDto;

import lombok.Data;
//프론트엔드에게 전달해주는 정보(유저에 대한 정보 -> username을 기반으로한 서치)
@Data
public class MemberDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
}
