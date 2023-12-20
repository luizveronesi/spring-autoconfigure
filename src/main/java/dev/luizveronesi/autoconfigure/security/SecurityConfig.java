package dev.luizveronesi.autoconfigure.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.CorsFilter;

import dev.luizveronesi.autoconfigure.properties.CorsProperties;
import dev.luizveronesi.autoconfigure.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsProperties corsProperties;

    private final SecurityProperties securityProperties;

    private final IUserService userService;
    
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable()).headers(headers -> headers.frameOptions().sameOrigin())
        	.exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint);

        if (corsProperties.getEnabled()) {
            http.cors(withDefaults())
                    .authorizeRequests(r -> r.requestMatchers(CorsUtils::isPreFlightRequest).permitAll());
        }

        if (BooleanUtils.isTrue(securityProperties.getDisabled())) {
            http.authorizeRequests(r -> r.anyRequest().permitAll())
                    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        } else {
            if (securityProperties.getPublicResourcesUri() != null
                    && securityProperties.getPublicResourcesUri().length > 0) {
                http.authorizeRequests(r -> r.antMatchers(securityProperties.getPublicResourcesUri()).permitAll());
            }

            http.authorizeRequests(r -> r
                    .antMatchers("/**/open/**").permitAll()
                    .antMatchers("/open/**").permitAll()
                    .anyRequest().authenticated())
                    .addFilterAfter(getJWTAuthorizationFilter(http), CorsFilter.class)
                    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        }
        return http.build();
    }

    private JwtAuthorizationFilter getJWTAuthorizationFilter(HttpSecurity http) throws Exception {
        return new JwtAuthorizationFilter(userService);
    }
}
