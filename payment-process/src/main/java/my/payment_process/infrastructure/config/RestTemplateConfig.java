package my.payment_process.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private RestTemplate restTemplate;

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
