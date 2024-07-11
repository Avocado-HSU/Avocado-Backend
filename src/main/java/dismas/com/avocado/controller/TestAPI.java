package dismas.com.avocado.controller;

import dismas.com.avocado.config.openFeign.FreeDictionary.FreeDictionaryApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestAPI {

    private final FreeDictionaryApiClient freeDictionaryApiClient;

    @GetMapping("test/freeDictionary/{word}")
    public String testFreeDictionary(@PathVariable String word) {
        return freeDictionaryApiClient.getWord(word);
    }
}
