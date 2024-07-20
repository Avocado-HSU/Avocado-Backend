package dismas.com.avocado.repository.attendance;

import dismas.com.avocado.domain.attendance.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DateRepository extends JpaRepository<Day, Long> {
    Optional<Day> findByDate(LocalDate date);
}
