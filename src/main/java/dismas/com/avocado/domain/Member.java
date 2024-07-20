package dismas.com.avocado.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dismas.com.avocado.domain.character.Character;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 엔티티 정의
 *
 * @version 1.0
 * @since 2024-07-08
 */
@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Jacksonized
@Getter
@Setter
public class Member extends BaseEntity{
    //추후 중복되는 멤버변수는 제거 예정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String name;

    private String role;

    private String OauthId;

    private String nickName;

    private String email;

    private String profileUrl;
    
    //해당 포인트는 유료 캐릭터 구매에 사용하는 포인트
    private Long point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Character> customCharacters = new ArrayList<>();

    /**
     * 사용자의 포인트를 증가시킵니다.
     * @param plusPoint 기존 포인트에서 증가시킬 포인트의 양
     */

    public void plusMemberPoint(Long plusPoint){
        point += plusPoint;
    }

    /**
     * 사용자의 포인트를 감소시킵니다.
     * @param minusPoint 기존 포인트에서 감소시킬 포인트의 양
     * // @throws PointOutOfBoundsException
     * //       if {@Code minusPoint} is greater than {@Code point}
     */
    public void minusMemberPoint(Long minusPoint){

            point -= minusPoint;
    }

}
