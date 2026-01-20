package com.greenthumb.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    //http://localhost:8080/swagger-ui/index.html#

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GreenThumb API")
                        .version("1.0")
                        .description("API documentation for the GreenThumb plant management application"));
    }
}
