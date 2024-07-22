package dismas.com.avocado.dto.mainPage;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "메인 페이지 Response Dto: api/main/{id}에서 메인 페이지를 위한 렌더링 정보 반환")
public class MainPageResponseDto {

    @Schema(description = "캐릭터 이미지 URL")
    public String characterImageUrl;

    @Schema(description = "캐릭터 메세지")
    public String message;

    @Schema(description = "주간 출석 여부")
    public WeeklyAttendanceDto weeklyAttendanceDto;

    @Schema(description = "인기 검색어 리스트")
    public PopularWordDto popularWordDto;

    @Schema(description = "추천 단어 리스트")
    public RecommendWordDto recommendWordDto;




}
