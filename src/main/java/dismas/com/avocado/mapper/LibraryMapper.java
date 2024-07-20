package dismas.com.avocado.mapper;

import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.LibraryDeleteResponseDTO;
import dismas.com.avocado.dto.libraryPage.LibraryResponseDto;
import dismas.com.avocado.dto.libraryPage.LibraryPageResponseDto;
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
    public List<LibraryResponseDto> toLibraryDtos(List<MemberWord> memberWords) {
        return memberWords.stream()
                .map(memberWord -> LibraryResponseDto.builder()
                        .English(memberWord.getWord().getEnglish())
                        .Korean(memberWord.getWord().getKorean())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * LibraryPageDto convert Mapper
     * - 캐릭터 이미지와 라이브러리 단어로 구성
     * @param libraryResponseDtos 라이브러리 단어 리스트
     * @param imgUrl 캐릭터 이미지
     * @return LibraryPageDto 라이브러리 페이지 데이터 전달
     */
    public LibraryPageResponseDto toLibraryPageDto(List<LibraryResponseDto> libraryResponseDtos, String imgUrl){
        return LibraryPageResponseDto.builder()
                .libraryResponseDtoList(libraryResponseDtos)
                .characterImgUrl(imgUrl)
                .build();
    }

    /**
     * LibraryDeleteResponseDto convert Mapper
     * 라이브러리 삭제 여부 전송 DTO 구성
     */
    public LibraryDeleteResponseDTO toLibraryDeleteResponseDto(boolean isDeleted) {
        return LibraryDeleteResponseDTO.builder().isDeleted(isDeleted).build();
    }
}
