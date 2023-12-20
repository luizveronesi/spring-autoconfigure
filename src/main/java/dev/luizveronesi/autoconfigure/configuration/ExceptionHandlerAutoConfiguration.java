package dev.luizveronesi.autoconfigure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.luizveronesi.autoconfigure.exception.ApplicationExceptionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ExceptionHandlerAutoConfiguration {

    @Bean
    public ApplicationExceptionHandler applicationExceptionHandler() {
        return new ApplicationExceptionHandler();
    }

}
