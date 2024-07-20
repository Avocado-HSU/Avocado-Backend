package dismas.com.avocado.repository.character;

import dismas.com.avocado.domain.character.MemberCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemeberCharacterRepository extends JpaRepository<MemberCharacter, Long> {
    MemberCharacter findByMemberIdAndIsSelectedTrue(Long memberId);
}

