package dismas.com.avocado.repository.attendance;

import dismas.com.avocado.domain.attendance.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}
