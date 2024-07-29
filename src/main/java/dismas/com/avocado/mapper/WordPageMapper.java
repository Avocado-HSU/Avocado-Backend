package dismas.com.avocado.mapper;

import dismas.com.avocado.dto.parsingPage.WordMultiDto;
import dismas.com.avocado.dto.wordPage.SearchRequestType;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import org.springframework.stereotype.Component;

import java.util.Map;
// 변경
@Component
public class WordPageMapper {

    public SearchWordResponseDto toSearchWordResponseDto(
            Boolean isSuccess, Boolean isLibraryRegistered, Long libraryId, String imgUrl, WordMultiDto contents, String korean){

        return SearchWordResponseDto.builder()
                .isSuccess(isSuccess)
                .isLibraryRegistered(isLibraryRegistered)
                .libraryId(libraryId)
                .korean(korean)
                .characterImgUrl(imgUrl)
                .contents(contents)
                .build();
    }



}
