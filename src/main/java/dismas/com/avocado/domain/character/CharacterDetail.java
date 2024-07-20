package dismas.com.avocado.domain.character;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * 캐릭터 디테일 엔티티 정의
 * 각 레벨 별 캐릭터 디테일 정리
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
@Setter
public class CharacterDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_detail_id")
    private Long id;

    private Long level;

    private String prefix;

    // 캐릭터 이름 필드(어른 아보카도건 애기 아보카도건 아보카도가 이름이므로)
    private String name;

    private String description;

    //성장에 필요한 포인트 필드
    private Long requiredPoint;

    //해당 캐릭터의 요금
    private Long price;

    private String imageUrl;

}
