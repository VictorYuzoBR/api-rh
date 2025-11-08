package com.rh.api_rh.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

// this class will be a bean and spring will handle it
@Configuration
public class CORSConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // apply cors in all endpoints
        registry.addMapping("/**")
                // because the front-end is local
                .allowedOrigins("http://localhost:3000","https://bikube-frontend.vercel.app/")
                // type os requests allowed
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // headers allowed, authorzation for the barer token, content type for json and
                // accept is additional
                .allowedHeaders("Authorization", "Content-Type", "Accept")
                // it must be settled as true because the aplication uses authentication
                .allowCredentials(true)
                // it means that the browser will cache the allowed cors configurations for one
                // hour before it request it again to the server - OPTIONS http method
                .maxAge(3600);

    }
}