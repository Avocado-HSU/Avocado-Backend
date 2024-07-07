package dismas.com.avocado.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckAPI {

    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }

}
