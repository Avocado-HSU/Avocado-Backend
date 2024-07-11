package dismas.com.avocado.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginTestController {

    @GetMapping("/yaho")
    public String loginSuccess(){
        return "로그인 성공";
    }

}
