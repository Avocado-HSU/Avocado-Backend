package dismas.com.avocado.repository.character;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.character.MemberCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberCharacterRepository extends JpaRepository<MemberCharacter, Long> {

    @Query("select mc from MemberCharacter mc join fetch mc.character where mc.member = :member and mc.isSelected = true")
    Optional<MemberCharacter> findMemberCharacter(@Param("member") Member member);

}

