package dismas.com.avocado.dto.libraryPage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Library Word Dto
 * 서비스 계층에서 Mapping 수행
 */
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Schema(description = "라이브러리 단어 Dto: api/library/{id}에서 LibraryWordPageDto에 리스트 형식으로 담겨서 반환")
public class LibraryWordDto {

    @Schema(description = "라이브러리 등록 시간")
    public LocalDateTime libraryUpdatedTime;
    @Schema(description = "해당 단어의 라이브러리 ID")
    public Long libraryId;
    @Schema(description = "영어 단어")
    public String english;
    @Schema(description = "영어 단어 한글 해석")
    public String korean;
    @Schema(description = "어원 리스트를 String List로 반환")
    public List<String> etymologyList;

}
