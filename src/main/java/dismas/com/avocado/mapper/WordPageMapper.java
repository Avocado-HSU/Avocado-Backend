package dismas.com.avocado.mapper;

import dismas.com.avocado.controller.SearchRequestType;
import dismas.com.avocado.dto.wordPage.SearchWordResponseDto;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WordPageMapper {

    public SearchWordResponseDto toSearchWordResponseDto(
            Boolean isSuccess, Long libraryId, String imgUrl, Map<SearchRequestType, String> contents){

        return SearchWordResponseDto.builder()
                .isSuccess(isSuccess)
                .libraryId(libraryId)
                .characterImgUrl(imgUrl)
                .contents(contents)
                .build();
    }



}
