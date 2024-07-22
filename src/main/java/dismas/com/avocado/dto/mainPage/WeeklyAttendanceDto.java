package dismas.com.avocado.dto.mainPage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "주간 출석 여부")
public class WeeklyAttendanceDto {
    @Schema(description = "index 0-6 = 월-일, true/false로 출석 여부 구분")
    public List<Boolean> attendances;
}
