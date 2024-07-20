package dismas.com.avocado.domain.character;

import dismas.com.avocado.domain.BaseEntity;
import dismas.com.avocado.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Jacksonized
@Getter
public class MemberCharacter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_character_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    private Long currentPoint;
    private String nickname;

    public void increaseCurrentPoint(Long plusPoint){
        currentPoint += plusPoint;
    }

    public void updateNickName(String newNickname){
        nickname = newNickname;
    }
}
