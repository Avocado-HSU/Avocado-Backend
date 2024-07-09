package dismas.com.avocado.repository.word;

import dismas.com.avocado.domain.word.MemberWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWordRepository extends JpaRepository<MemberWord, Long> {
}
