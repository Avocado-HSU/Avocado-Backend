package dismas.com.avocado.dto.parsingPage;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WordMeanDto {
    private Map<String, String> meanings; // 각 품사와 해당 설명을 저장
    private String greetingMsg; // 인사 메시지

}
