package dismas.com.avocado.repository.word;

import dismas.com.avocado.domain.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
