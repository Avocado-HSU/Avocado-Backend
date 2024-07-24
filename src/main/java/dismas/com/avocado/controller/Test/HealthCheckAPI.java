package dismas.com.avocado.controller.Test;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HealthCheckAPI {

    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }

}
