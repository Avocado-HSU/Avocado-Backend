package dismas.com.avocado.dto.chatBotPage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "ChatBotAPI : api/chatbot/request/{requestType}/{word} 반환 DTO")
public class ChatBotResponseDto {

    @Schema(description = "검색 성공 여부")
    public boolean isSuccess;
    @Schema(description = "검색 성공 시 반환되는 단어 관련 정보")
    public String content;
}
