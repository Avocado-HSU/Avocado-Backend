package dismas.com.avocado.repository.word;

import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.domain.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberWordRepository extends JpaRepository<MemberWord, Long> {

    @Query("select m from MemberWord m where m.word = :word")
    Optional<MemberWord> findByWord(Word word);

}
