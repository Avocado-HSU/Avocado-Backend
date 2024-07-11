package dismas.com.avocado.config.openFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FreeDictionary", url = "https://api.dictionaryapi.dev/api/v2/entries/en")
public interface FreeDictionaryApiClient {

    @GetMapping("/{word}")
    String getWord(@PathVariable("word") String word);

}
