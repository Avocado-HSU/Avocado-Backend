package dismas.com.avocado.dto.libraryPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

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
public class LibraryWordDto {

    public Long LibraryId;
    public String English;
    public String Korean;
    public List<String> etymologyList;

}
