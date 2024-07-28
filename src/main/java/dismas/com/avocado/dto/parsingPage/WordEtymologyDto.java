package dismas.com.avocado.dto.parsingPage;

import lombok.Data;

@Data
public class WordEtymologyDto {
    private String etymology;   // 어원
    private String root;        // 어근
    private String prefix;      // 접두사
    private String suffix;      // 접미사
    private String korean;       // 한글뜻
    private String etymologyDescription; // 어원 설명
    private String rootDescription; // 어근 설명
    private String prefixDescription; // 접두사 설명
    private String suffixDescription; //접미사 설명
}
