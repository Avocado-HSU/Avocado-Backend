package dismas.com.avocado.repository.attendance;

import dismas.com.avocado.domain.attendance.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DateRepository extends JpaRepository<Date, Long> {
    Date findByDate(LocalDate date);
}
