package dismas.com.avocado.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * 사용자 엔티티 정의
 *
 * @version 1.0
 * @since 2024-07-08
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Jacksonized
@Getter
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String OauthId;

    private String email;

    private String profileUrl;

    private Long point;

    /**
     * 사용자의 포인트를 증가시킵니다.
     *
     * @param plusPoint 기존 포인트에서 증가시킬 포인트의 양
     */

    public void plusMemberPoint(Long plusPoint){
        point += plusPoint;
    }

    /**
     * 사용자의 포인트를 감소시킵니다.
     *
     * @param minusPoint 기존 포인트에서 감소시킬 포인트의 양
     * // @throws PointOutOfBoundsException
     * //       if {@Code minusPoint} is greater than {@Code point}
     */
    public void minusMemberPoint(Long minusPoint){
        if(point > minusPoint){
            // 예외처리
        }
        else{
            point -= minusPoint;
        }
    }

}
