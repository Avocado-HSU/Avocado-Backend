package dismas.com.avocado.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
public class CharacterDTO {

    private Long id;
    private String name;
    private Long price;
    private Long level;
    private String prefix;
    private String description;
    private String imageUrl;
    //현재 포인트
    private Long currentPoint;
    //성장에 필요한 포인트
    private Long requiredPoint;


}
