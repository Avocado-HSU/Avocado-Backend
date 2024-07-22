package dismas.com.avocado.domain.character;


import dismas.com.avocado.domain.Member;
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

    private String description;

    //성장에 필요한 포인트 필드
    private Long requiredPoint;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

}
