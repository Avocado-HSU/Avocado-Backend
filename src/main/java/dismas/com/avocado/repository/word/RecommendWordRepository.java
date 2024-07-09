package dismas.com.avocado.repository.word;

import dismas.com.avocado.domain.word.RecommendWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendWordRepository extends JpaRepository<RecommendWord, Long> {
}
