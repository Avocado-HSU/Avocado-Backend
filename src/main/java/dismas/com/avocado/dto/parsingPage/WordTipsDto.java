package dismas.com.avocado.dto.parsingPage;

import lombok.Data;

@Data
public class WordTipsDto {
    private String mnemonicMethod; // 연상 기억법
    private String wordAnalysis;    // 단어 분해
    private String spacedRepetition; // 반복 학습
    private String storytelling;    // 스토리텔링
}
