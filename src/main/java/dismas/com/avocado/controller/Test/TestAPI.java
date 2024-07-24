package dismas.com.avocado.controller.Test;

import dismas.com.avocado.config.openFeign.FreeDictionary.FreeDictionaryApiClient;
import dismas.com.avocado.domain.Member;
import dismas.com.avocado.service.CharacterService;
import dismas.com.avocado.service.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequiredArgsConstructor
public class TestAPI {

    private final FreeDictionaryApiClient freeDictionaryApiClient;

    private final CharacterService characterService;
    private final MemberService memberService;

    @GetMapping("test/freeDictionary/{word}")
    public String testFreeDictionary(@PathVariable String word) {
        return freeDictionaryApiClient.getWord(word);
    }

    /**
     * 사용자 및 캐릭터 레벨업 Test
     *
     * @param member 사용자 ID
     * @param increasePoint 증가하는 포인트
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/exp-up/{id}/{point}")
    public void expUpCharacter(
            @PathVariable("id") Member member,
            @PathVariable("point") Long increasePoint
            ) {
        memberService.updatePoint(member, increasePoint);
    }
}
