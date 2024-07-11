package dismas.com.avocado.domain.character;

import dismas.com.avocado.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;


/**
 * 캐릭터 엔티티 정의
 * 캐틱터의 공통 정보들을 저장한다.
 * 각 레벨별 캐릭터의 특징은 1:N 관계로 CharacterDetail 에 저장한다
 *
 * @version 1.0
 * @since 2024-07-08
 */
@Entity

//@Table(name = "`character`") //MySQL 예약어 충돌 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Jacksonized
@Getter
//원래 Character였으나 mysql에서 작업 시 충돌로 인하여 임시 변경
public class CustomCharacter extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Long id;

    private String name;
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_detail_id")
    private CharacterDetail characterDetail;
}
