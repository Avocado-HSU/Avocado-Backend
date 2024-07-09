package dismas.com.avocado.repository.attendance;

import dismas.com.avocado.domain.attendance.Date;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateRepository extends JpaRepository<Date, Long> {

}
