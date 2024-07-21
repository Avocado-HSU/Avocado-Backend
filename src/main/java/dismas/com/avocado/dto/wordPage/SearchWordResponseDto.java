package dismas.com.avocado.dto.wordPage;

import dismas.com.avocado.controller.SearchRequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchWordResponseDto {
    Boolean isSuccess;
    Long libraryId;
    String characterImgUrl;
    Map<SearchRequestType, String> contents;
}
