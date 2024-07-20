package dismas.com.avocado.mapper;

import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.searchPage.RecentSearchWordResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchMapper {

    public RecentSearchWordResponseDto toRecentSearchWordResponseDto(List<MemberWord> memberWords){
        List<String> recentSearchWords = memberWords.stream()
                .map(memberWord -> memberWord.getWord().getEnglish())
                .collect(Collectors.toList());

        return new RecentSearchWordResponseDto(recentSearchWords);
    }

}
