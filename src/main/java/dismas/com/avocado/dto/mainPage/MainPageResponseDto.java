package dismas.com.avocado.dto.mainPage;


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
public class MainPageResponseDto {

    // 상단 프로필

    // 캐릭터 이미지 및 텍스트
    public String characterImageUrl;

    public String message;

    // 주간 출석 내역
    public WeeklyAttendanceDto weeklyAttendanceDto;

    // 인기 검색어
    public PopularWordDto popularWordDto;

    // 추천 검색어
    public RecommendWordDto recommendWordDto;




}
