package dismas.com.avocado.repository.word;

import dismas.com.avocado.domain.word.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("select w from Word w where w.english = :word")
    Optional<Word> findWordByString(@Param("word")String word);

    @Query("select w.id from Word w where w.english = :word")
    Optional<Long> findIdByWord(@Param("word")String word);

    // 검색량이 가장 높은 상위 5개 단어 조회, 카운트 Query 사용 X
    @Query("select w from Word w order by w.searchCount desc")
    List<Word> findPopularWord(Pageable pageable);
}
