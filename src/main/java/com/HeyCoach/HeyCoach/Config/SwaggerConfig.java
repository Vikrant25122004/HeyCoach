package com.HeyCoach.HeyCoach.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.awt.SystemColor.info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI mycustomconfig(){
        return new OpenAPI().info(
                new Info().title("Hey Coach docs")
                        .description("Hey Coach is an platform where you can search all jobs on linked in and see the job market trend going and generate resume according to the particular jobs description and download also you can generate mock interview question and answers in free")
        );
    }
}
