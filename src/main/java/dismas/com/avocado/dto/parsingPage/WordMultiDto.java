package dismas.com.avocado.dto.parsingPage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WordMultiDto {

    public WordTipsDto wordTipsDto;
    public WordEtymologyDto wordEtymologyDto;
    public WordMeanDto wordMeanDto;

}
