package dismas.com.avocado.dto.libraryPage;
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
@Schema(description = "LibraryPageAPI: api/library/{id} 반환 Dto")
public class LibraryPageResponseDto {

    @Schema(description = "캐릭터 이미지 (ex 아보카) URL 반환")
    public String characterImgUrl;

    @Schema(description = "라이브러리 단어 리스트")
    public List<LibraryWordDto> libraryWordDtoList;

}