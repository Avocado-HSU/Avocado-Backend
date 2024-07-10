package dismas.com.avocado.repository.character;

import dismas.com.avocado.domain.character.CustomCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCharacterRepository extends JpaRepository<CustomCharacter, Long> {
}
