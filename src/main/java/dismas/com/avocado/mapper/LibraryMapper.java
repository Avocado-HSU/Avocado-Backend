package dismas.com.avocado.mapper;

import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.LibraryDto;
import dismas.com.avocado.dto.libraryPage.LibraryPageDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component

public class LibraryMapper {

    /**
     * Library to LibraryDto convert Mapper
     * - 추후 etymologyList 파싱 나올 시 로직 추가할 것
     * @param memberWords
     * @return List<LibraryDto> LibraryPageDto 생성을 위함
     */
    public List<LibraryDto> toLibraryDtos(List<MemberWord> memberWords) {
        return memberWords.stream()
                .map(memberWord -> LibraryDto.builder()
                        .English(memberWord.getWord().getEnglish())
                        .Korean(memberWord.getWord().getKorean())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * LibraryPageDto convert Mapper
     * - 캐릭터 이미지와 라이브러리 단어로 구성
     * @param libraryDtos 라이브러리 단어 리스트
     * @param imgUrl 캐릭터 이미지
     * @return LibraryPageDto 라이브러리 페이지 데이터 전달
     */
    public LibraryPageDto toLibraryPageDto(List<LibraryDto> libraryDtos, String imgUrl){
        return LibraryPageDto.builder()
                .libraryDtoList(libraryDtos)
                .characterImgUrl(imgUrl)
                .build();
    }
}
