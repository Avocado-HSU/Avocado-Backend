package dismas.com.avocado.domain.word;


import dismas.com.avocado.domain.BaseEntity;
import dismas.com.avocado.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * 사용자 단어 엔티티 정의
 * 단어에 대한 사용자 통계 및 기타 메타정보를 저장한다.
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
public class MemberWord extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_word_id")
    private Long id;

    private Long searchCount;    // 사용자 검색 횟수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean isLibraryWord;


    public void registerLibraryWord() {
        isLibraryWord = true;
    }

    public void unregisterLibraryWord() {
        isLibraryWord = false;
    }

    public void plusMemberWordSearchCount(){
        searchCount++;
    }
}
