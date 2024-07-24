package dismas.com.avocado.dto.wordPage;

import dismas.com.avocado.dto.parsingPage.WordMultiDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "검색 시 반환하는 DTO, 해당 DTO를 기반으로 Word Page를 렌더링한다.")
public class SearchWordResponseDto {

    @Schema(description = "검색 성공 여부")
    Boolean isSuccess;

    @Schema(description = "해당 단어가 라이브러리에 등록되어 있는지 여부 " +
            "등록되어 있을 경우 : true - 라이브러리 등록 버튼 활성화 " +
            "등록되지 아니할 경우 : false - 라이브러리 등록 버튼 비활성화 or 배너")
    Boolean isLibraryRegistered;

    @Schema(description = "라이브러리 단어 등록/삭제 요청을 위한 ID")
    Long libraryId;

    @Schema(description = "한글 뜻")
    String korean;

    @Schema(description = "캐릭터 이미지")
    String characterImgUrl;

    // 해당 내용 변경 -> Map<SearchRequestType, String> -> WordMultiDto
    @Schema(description = "OpenAI의 단어 분석 결과, Enum 타입으로 구분",
            allowableValues = {"MEANS", "SEPARATE", "TIP"})
    WordMultiDto contents;
}
