package dismas.com.avocado.mapper;

import dismas.com.avocado.domain.word.PopularWord;
import dismas.com.avocado.dto.mainPage.PopularWordResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainPageMapper {

    public PopularWordResponseDto toPopularWordDto(List<PopularWord> words) {
        List<String> popularWords = words.stream()
                .map(PopularWord::getEnglish)
                .toList();

        return PopularWordResponseDto.builder()
                .popularWords(popularWords)
                .build();
    }
}
