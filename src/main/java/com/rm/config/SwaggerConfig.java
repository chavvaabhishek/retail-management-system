package com.rm.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI retailManagementOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()

                                .title("Retail Management System API")

                                .description(
                                        "Enterprise Retail Management Backend APIs"
                                )

                                .version("1.0")

                                .contact(
                                        new Contact()
                                                .name("Abhishek Chavva")
                                                .email("chavvaabhishek20@gmail.com")
                                )

                                .license(
                                        new License()
                                                .name("Open Source")
                                )
                )

                .externalDocs(

                        new ExternalDocumentation()

                                .description(
                                        "Retail Management Documentation"
                                )
                );
    }
}