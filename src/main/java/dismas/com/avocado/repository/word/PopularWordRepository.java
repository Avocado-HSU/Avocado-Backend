package dismas.com.avocado.repository.word;

import dismas.com.avocado.domain.word.PopularWord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopularWordRepository extends JpaRepository<PopularWord, Long> {

    // 삭제 쿼리
    void deleteAll();

    // 상위 5개 조회 쿼리
    @Query("select pw from PopularWord pw order by pw.id desc")
    List<PopularWord> findPopularWord(Pageable pageable);

}
