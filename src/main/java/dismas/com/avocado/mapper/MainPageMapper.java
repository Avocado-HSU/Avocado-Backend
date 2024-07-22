package dismas.com.avocado.mapper;

import dismas.com.avocado.domain.attendance.Attendance;
import dismas.com.avocado.domain.word.PopularWord;
import dismas.com.avocado.domain.word.Word;
import dismas.com.avocado.dto.WordDto;
import dismas.com.avocado.dto.mainPage.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainPageMapper {

    public MainPageResponseDto toMainPageResponseDto(
            MainPageCharacterDto mainPageCharacterDto,
            WeeklyAttendanceDto weeklyAttendanceDto,
            PopularWordDto popularWordDto,
            RecommendWordDto recommendWordDto
    ){
        // 프로필 및 캐릭터 이미지 추가 필요
        return MainPageResponseDto.builder()
                .characterImageUrl(mainPageCharacterDto.getImgUrl())
                .message(mainPageCharacterDto.getMessage())
                .weeklyAttendanceDto(weeklyAttendanceDto)   // 주간 출석 체크
                .popularWordDto(popularWordDto)             // 인기 검색어
                .recommendWordDto(recommendWordDto)         // 추천 검색어
                .build();
    }

    public PopularWordDto toPopularWordDto(List<PopularWord> words) {
        List<String> popularWords = words.stream()
                .map(PopularWord::getEnglish)
                .toList();

        return PopularWordDto.builder()
                .popularWords(popularWords)
                .build();
    }


    public WeeklyAttendanceDto toAttendanceDto(List<Attendance> attendances){
        List<Boolean> attendanceList = new ArrayList<>(Collections.nCopies(7, false));

        attendances.stream()
                .map(attendance -> attendance.getDay().getDate().getDayOfWeek().getValue() - 1)
                .distinct()
                .forEach(index -> attendanceList.set(index, true));

        return WeeklyAttendanceDto.builder()
                .attendances(attendanceList)
                .build();
    }

    public RecommendWordDto toRecommendWordDto(List<Word> random5Words){
        List<WordDto> recommendWords = random5Words.stream()
                .map(word -> new WordDto(
                        word.getEnglish(),
                        word.getKorean(),
                        word.getEtymology()))
                .collect(Collectors.toList());

        return RecommendWordDto.builder()
                .recommendWords(recommendWords)
                .build();
    }
}
