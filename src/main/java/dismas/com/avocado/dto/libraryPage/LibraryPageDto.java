package dismas.com.avocado.dto.libraryPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LibraryPageDto {

    // 캐릭터
    public String characterImgUrl;
    // 라이브러리 단어 리스트
    public List<LibraryDto> libraryDtoList = new ArrayList<LibraryDto>();

}
