package dismas.com.avocado.mapper;

import dismas.com.avocado.dto.chatBotPage.ChatBotResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ChatBotMapper {

    public ChatBotResponseDto toChatBotResponseDto(boolean isSuccess, String content){
        return ChatBotResponseDto.builder()
                .isSuccess(isSuccess)
                .content(content)
                .build();
    }

}
