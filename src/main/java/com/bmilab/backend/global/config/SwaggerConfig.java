package com.bmilab.backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "BMI-LAB Web API",
                description = "BMI-LAB을 관리하기 위한 웹 서비스",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");
        Components components = new Components().addSecuritySchemes("JWT", new SecurityScheme()
                .name("JWT")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
        return new OpenAPI()
                .components(new Components())
                .servers(List.of(
                        new Server()
                                .url("https://api.snuh-bmilab.ai.kr")
                                .description("Production"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local"),
                        new Server()
                                .url("https://dev-api.snuh-bmilab.ai.kr")
                                .description("Development")
                ))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
