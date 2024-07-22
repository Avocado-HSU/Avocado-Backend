package dismas.com.avocado.dto.libraryPage;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "라이브러리 단어 등록/삭제 Dto에서 등록, 삭제, 에러 여부를 ENUM으로 반환")
public enum UpdateLibraryResponseType {
    @Schema(description = "라이브러리 단어 등록 성공")
    REGISTERED,
    @Schema(description = "라이브러리 단어 삭제 성공")
    DELETED,
    @Schema(description = "에러 발생")
    ERROR
}