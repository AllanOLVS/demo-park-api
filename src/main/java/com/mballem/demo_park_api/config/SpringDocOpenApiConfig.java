package com.mballem.demo_park_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI apenApi(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("REST API - SPRING PARK")
                                .description("API para gestão de estacionamento de veiculos")
                                .version("v1")
                                .license(new License().name("Apache 2.0").url("apache.org/licenses/LICENSE-2.0"))
                                .contact(new Contact().name("Allan Oliveira Santos").email("allan.oliveiraa009@gmail.com"))
                );
    }

}
