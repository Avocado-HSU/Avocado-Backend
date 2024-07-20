package dismas.com.avocado.repository.attendance;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.attendance.Attendance;
import dismas.com.avocado.domain.attendance.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

    @Query("select a from Attendance a  where a.member = :member and a.date.date =:date" )
    Attendance findToDay(@Param("member")Member member, @Param("date")LocalDate date);

    @Query("select a from Attendance a where a.member = :member and a.date.date between :start and :end")
    List<Attendance> findByWeek(@Param("member")Member member, @Param("start")LocalDate start, @Param("end")LocalDate end);


}
