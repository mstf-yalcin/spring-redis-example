package com.demo.redisexample.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearer-key")})
//@io.swagger.v3.oas.annotations.security.SecurityScheme(name = "bearer-key",description = "Jwt auth desc",scheme = "bearer",type = SecuritySchemeType.HTTP,bearerFormat = "JWT",in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getApiInfo("Redis-Test"))
                .components(securityScheme());
    }

    private Components securityScheme()
    {
        return new Components()
                .addSecuritySchemes("bearer-key",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
    }

    private Info getApiInfo(String appName) {
        return new Info().title(appName).description(appName).contact(getContact()).version("1.0");
    }

    private Contact getContact() {
        return new Contact().name("name").email("email").url("url");
    }


}


