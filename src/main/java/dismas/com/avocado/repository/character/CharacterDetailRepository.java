package dismas.com.avocado.repository.character;

import dismas.com.avocado.domain.character.CharacterDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterDetailRepository extends JpaRepository<CharacterDetail, Long> {
    //CharacterDetail findByLevelAndName(Long level, String name);
   // CharacterDetail findFirstByNameOrderByLevelAsc(String name);

   // Optional<CharacterDetail> findByCharacterAndLevel(Character character, Long level);
    List<CharacterDetail> findByCharacterIdOrderByLevelAsc(Long characterId);

}
