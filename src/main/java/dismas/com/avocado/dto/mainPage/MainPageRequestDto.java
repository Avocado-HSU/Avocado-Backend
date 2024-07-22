package dismas.com.avocado.dto.mainPage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "메인 페이지 Request DTO: api/main/{id} 요청 시 json 으로 전달")
public class MainPageRequestDto {

    @NotEmpty
    @Schema(description = "출석체크 여부 반환을 위한 TimeStamp", example = "2024-07-22")
    public LocalDate date;
}
