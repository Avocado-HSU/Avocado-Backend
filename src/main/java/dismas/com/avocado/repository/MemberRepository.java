package dismas.com.avocado.repository;

import dismas.com.avocado.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
