package dismas.com.avocado;

import dismas.com.avocado.domain.character.Character;
import dismas.com.avocado.domain.character.CharacterDetail;
import dismas.com.avocado.repository.character.CharacterDetailRepository;
import dismas.com.avocado.repository.character.CharacterRepository;
import dismas.com.avocado.repository.word.WordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class AvocadoInit {


    private final CharacterRepository characterRepository;
    private final CharacterDetailRepository characterDetailRepository;
    private final WordRepository wordRepository;

    @PostConstruct
    public void init(){
        characterInit();
    }

    public void characterInit(){
        if(characterRepository.findById(1L).isEmpty()){

            Character avocado = Character.builder()
                    .id(1L)
                    .name("아보카")
                    .price(0L)
                    .build();

            characterRepository.save(avocado);

            characterDetailRepository.save(
                    CharacterDetail.builder()
                            .id(1L)
                            .character(avocado)
                            .level(1L)
                            .description("1레벨 아보카도")
                            .imageUrl("https://static.wikia.nocookie.net/pokemon/images/b/bb/%ED%94%BC%EC%B8%84_%EA%B3%B5%EC%8B%9D_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8.png/revision/latest?cb=20170406071056&path-prefix=ko")
                            .prefix("애기")
                            .requiredPoint(0L)
                            .build()
            );
            characterDetailRepository.save(
                    CharacterDetail.builder()
                            .id(2L)
                            .character(avocado)
                            .level(2L)
                            .description("2레벨 아보카도")
                            .imageUrl("https://static.wikia.nocookie.net/pokemon/images/5/52/%ED%94%BC%EC%B9%B4%EC%B8%84_%EA%B3%B5%EC%8B%9D_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8.png/revision/latest/scale-to-width-down/1000?cb=20170405000019&path-prefix=ko")
                            .prefix("유치원")
                            .requiredPoint(100L)
                            .build()
            );
            characterDetailRepository.save(
                    CharacterDetail.builder()
                            .id(3L)
                            .character(avocado)
                            .level(3L)
                            .description("3레벨 아보카도")
                            .imageUrl("https://static.wikia.nocookie.net/pokemon/images/3/3c/%EB%9D%BC%EC%9D%B4%EC%B8%84_%EA%B3%B5%EC%8B%9D_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8.png/revision/latest/scale-to-width-down/1000?cb=20170405000124&path-prefix=ko")
                            .prefix("청소년")
                            .requiredPoint(200L)
                            .build()
            );
            characterDetailRepository.save(
                    CharacterDetail.builder()
                            .id(4L)
                            .character(avocado)
                            .level(4L)
                            .description("4레벨 아보카도")
                            .imageUrl("https://static.wikia.nocookie.net/pokemon/images/c/c7/%EA%B1%B0%EB%8B%A4%EC%9D%B4%EB%A7%A5%EC%8A%A4_%ED%94%BC%EC%B9%B4%EC%B8%84_%EA%B3%B5%EC%8B%9D_%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8.png/revision/latest?cb=20191016132253&path-prefix=ko")
                            .prefix("대학생")
                            .requiredPoint(300L)
                            .build()
            );
            characterDetailRepository.save(
                    CharacterDetail.builder()
                            .id(5L)
                            .character(avocado)
                            .level(5L)
                            .description("5레벨 아보카도")
                            .imageUrl("https://mblogthumb-phinf.pstatic.net/20140414_110/dbswp1201_1397477670995xx76l_PNG/Pokelectronics.png?type=w2")
                            .prefix("졸업생")
                            .requiredPoint(400L)
                            .build()
            );
        }
    }
}
