package dismas.com.avocado.domain.character;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class CharacterDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_detail_id")
    private Long id;

    private Long level;

    private String prefix;
    private String description;

    private String imageUrl;

}
