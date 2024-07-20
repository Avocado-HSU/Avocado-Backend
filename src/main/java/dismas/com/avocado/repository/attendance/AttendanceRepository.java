package dismas.com.avocado.repository.attendance;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.attendance.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a  where a.member = :member and a.day.date =:date" )
    Optional<Attendance> findAttendanceByDate(@Param("member")Member member, @Param("date")LocalDate date);

    @Query("select a from Attendance a where a.member = :member and a.day.date between :start and :end")
    List<Attendance> findByWeek(@Param("member")Member member, @Param("start")LocalDate start, @Param("end")LocalDate end);

    @Query("select a from Attendance a join fetch a.day where a.member = :member and a.day.date between :monday and :sunday")
    List<Attendance> findWeeklyAttendanceByMember(
            @Param("member") Member member,
            @Param("monday") LocalDate monday,
            @Param("sunday") LocalDate sunday
    );


}
