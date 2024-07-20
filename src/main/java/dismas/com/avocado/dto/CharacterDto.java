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
    private Long price;
    private Long level;
    private String prefix;
    private String description;
    private String imageUrl;
    private Long currentPoint;
    private Long requiredPoint;
}
