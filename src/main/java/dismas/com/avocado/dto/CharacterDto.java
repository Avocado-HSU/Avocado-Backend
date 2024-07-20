package dismas.com.avocado.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CharacterDto {

    private Long id;
    private String name;
    private Long level;
    private String description;
    private String imageUrl;
    //현재 포인트
    private Long currentPoint;
    //성장에 필요한 포인트
    private Long requiredPoint;
}
