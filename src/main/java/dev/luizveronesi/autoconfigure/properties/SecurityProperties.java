package dev.luizveronesi.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

	private Boolean disabled;
    private String[] publicResourcesUri;

}
