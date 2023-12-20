package dev.luizveronesi.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    private Boolean enabled;

    private String[] allowedMethods;

    private String[] allowedHeaders;

    private String[] exposedHeaders;

    private String[] allowedOrigins;

}
