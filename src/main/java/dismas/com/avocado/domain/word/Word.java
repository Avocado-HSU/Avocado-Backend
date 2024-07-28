package dismas.com.avocado.domain.word;


import dismas.com.avocado.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * 단어 엔티티 정의
 *
 * @version 1.0
 * @since 2024-07-08
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Jacksonized
@Getter
public class Word extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;

    private Long searchCount;   // 인기 검색어

    private String english;
    private String korean;
    private String etymology;// 어원

    private String prefix;  // 접두사
    private String suffix;  // 접미사
    private String imageUrl;
    private String audioUrl;

    /**
     * 사용자가 검색한 횟수 증가
     * 추후 인기 검색어에 사용
     */
    public void plusWordSearchCount(){
        searchCount++;
    }
}
