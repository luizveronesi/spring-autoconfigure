package dev.luizveronesi.autoconfigure.configuration;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.luizveronesi.autoconfigure.properties.CorsProperties;
import dev.luizveronesi.autoconfigure.properties.SecurityProperties;
import dev.luizveronesi.autoconfigure.security.CustomAuthenticationEntryPoint;
import dev.luizveronesi.autoconfigure.security.IUserService;
import dev.luizveronesi.autoconfigure.security.SecurityConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties({ CorsProperties.class, SecurityProperties.class })
public class SecurityAutoConfiguration implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    private final SecurityProperties securityProperties;

    private final IUserService userService;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityConfig securityConfig() {
        return new SecurityConfig(
                corsProperties,
                securityProperties,
                userService,
                customAuthenticationEntryPoint);
    }

    @Bean
    @ConditionalOnProperty(value = "app.cors.enabled", havingValue = "true")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods()));
        config.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders()));
        config.setExposedHeaders(Arrays.asList(corsProperties.getExposedHeaders()));
        config.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins()));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
