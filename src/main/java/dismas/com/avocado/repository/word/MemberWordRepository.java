package dismas.com.avocado.repository.word;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.domain.word.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberWordRepository extends JpaRepository<MemberWord, Long> {

    // 추후 Collection 타입으로 관리하는 것을 고민해 보아야 한다.
    @Query("select m from MemberWord m where m.word = :word and m.member = :member")
    Optional<MemberWord> findByMemberAndWord(@Param("word") Word word, @Param("member") Member member);


    @Query("select mw from MemberWord mw join fetch mw.word where mw.member =:member order by mw.createdAt desc")
    Page<MemberWord> findByMember(@Param("member") Member member, Pageable pageable);

    /**
     * 라이브러리 단어 조회 메서드
     *
     * @param member 사용자
     * @param pageable 페이징
     * @return Page<MemberWord>
     */
    @Query("select mw from MemberWord mw join fetch mw.word where mw.member = :member and mw.isLibraryWord = true order by mw.createdAt desc")
    Page<MemberWord> findLibraryByMember(@Param("member") Member member, Pageable pageable);

    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query("delete from MemberWord m where m =: memberWord")
    void deleteByEntity(@Param("memberWord") MemberWord memberWord);



}
