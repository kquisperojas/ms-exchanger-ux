package com.exchanger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API ux Exchanger")
                        .description("API para realizar operaciones de cambio de moneda")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kevin Quispe")
                                .email("kevin@correo.com"))
                        .license(new License()
                                .name("MIT")));
    }
}
