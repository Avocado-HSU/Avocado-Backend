package dismas.com.avocado.repository.character;

import dismas.com.avocado.domain.character.CharacterDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterDetailRepository extends JpaRepository<CharacterDetail, Long> {
    CharacterDetail findByLevelAndName(Long level, String name);
    CharacterDetail findFirstByNameOrderByLevelAsc(String name);
}
