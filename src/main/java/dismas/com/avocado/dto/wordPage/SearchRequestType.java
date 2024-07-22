package dismas.com.avocado.dto.wordPage;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OpenAI의 분석 결과를 Enum 타입으로 분류")
public enum SearchRequestType {
    @Schema(description = "단어의 의미")
    MEANS,
    @Schema(description = "어원 분리")
    SEPARATE,
    @Schema(description = "어원을 통한 단어 암기 팁")
    TIP
}
