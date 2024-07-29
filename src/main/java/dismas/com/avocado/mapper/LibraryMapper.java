package dismas.com.avocado.mapper;


import dismas.com.avocado.domain.word.MemberWord;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseDto;
import dismas.com.avocado.dto.libraryPage.LibraryWordDto;
import dismas.com.avocado.dto.libraryPage.LibraryPageResponseDto;
import dismas.com.avocado.dto.libraryPage.UpdateLibraryResponseType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {

    /**
     * Library to LibraryDto convert Mapper
     * - 추후 etymologyList 파싱 나올 시 로직 추가할 것
     * @param memberWords 사용자 입력
     * @return List<LibraryDto> LibraryPageDto 생성을 위함
     */
    public List<LibraryWordDto> toLibraryDtos(List<MemberWord> memberWords) {

        return memberWords.stream()
                .map(memberWord -> {
                    List<String> etymologyList = new ArrayList<>();
                    if (memberWord.getWord().getEtymology() != null && !memberWord.getWord().getEtymology().isEmpty()) {
                        etymologyList.add(memberWord.getWord().getEtymology());
                    }
                    if (memberWord.getWord().getPrefix() != null && !memberWord.getWord().getPrefix().isEmpty()) {
                        etymologyList.add(memberWord.getWord().getPrefix());
                    }

                    return LibraryWordDto.builder()
                            .libraryId(memberWord.getId())
                            .english(memberWord.getWord().getEnglish())
                            .korean(memberWord.getWord().getKorean())
                            .etymologyList(etymologyList)
                            .libraryUpdatedTime(memberWord.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * LibraryPageDto convert Mapper
     * - 캐릭터 이미지와 라이브러리 단어로 구성
     * @param libraryWordDtos 라이브러리 단어 리스트
     * @param imgUrl 캐릭터 이미지
     * @return LibraryPageDto 라이브러리 페이지 데이터 전달
     */
    public LibraryPageResponseDto toLibraryPageDto(List<LibraryWordDto> libraryWordDtos, String imgUrl){
        return LibraryPageResponseDto.builder()
                .libraryWordDtoList(libraryWordDtos)
                .characterImgUrl(imgUrl)
                .build();
    }

    /**
     * LibraryDeleteResponseDto convert Mapper
     * 라이브러리 등록/삭제 여부 전송 DTO 구성
     */
    public UpdateLibraryResponseDto toUpdateLibraryResponseDto(UpdateLibraryResponseType responseType, Long libraryId){
        return UpdateLibraryResponseDto.builder()
                .responseType(responseType)
                .LibraryId(libraryId)
                .build();
    }
}