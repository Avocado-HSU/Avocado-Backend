package dismas.com.avocado.dto;

import dismas.com.avocado.domain.attendance.Attendance;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class WeekDTO {
    private int week;
    private List<Attendance> attendances;

    public WeekDTO(int week, List<Attendance> attendances) {
        this.week = week;
        this.attendances = attendances;
    }
}
