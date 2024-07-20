package dismas.com.avocado.repository.character;

import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.domain.character.CharacterDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterDetailRepository extends JpaRepository<CharacterDetail, Long> {

    List<CharacterDetail> findByCharacterIdOrderByLevelAsc(Long characterId);

    @Query("select cd from CharacterDetail cd where cd.level = :level and cd.character = :character")
    Optional<CharacterDetail> findByLevel(@Param("character") Character character, @Param("level") Long level);
}
