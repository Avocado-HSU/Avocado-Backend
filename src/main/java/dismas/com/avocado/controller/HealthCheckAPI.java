package dismas.com.avocado.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HealthCheckAPI {

    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }

}
