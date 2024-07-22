package dismas.com.avocado.dto.libraryPage;


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
@Schema(description = "라이브러리 단어 등록/삭제 Dto: api/word/library/{libraryId}에서 라이브러리 단어 등록/삭제 여부 반환")
public class UpdateLibraryResponseDto {

    @Schema(description = "단어 등록/삭제 여부 반환", allowableValues = {"REGISTERED", "DELETED", "ERROR"})
    UpdateLibraryResponseType responseType;

    Long LibraryId;
}
