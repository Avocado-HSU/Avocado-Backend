package dismas.com.avocado.dto.chatBotPage;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챗봇 요청 양식에 대한 ENUM 객체이다")
public enum ChatBotRequestType {
    @Schema(description = "단어 정의 질의")
    DEFINE,
    @Schema(description = "유사 단어 질의")
    SIMILAR,
    @Schema(description = "단어 어원 질의")
    ETYMOLOGY,
    @Schema(description = "단어 접미사 질의")
    PREFIX,
    @Schema(description = "단어 암기 방법 질의")
    TIP
}
