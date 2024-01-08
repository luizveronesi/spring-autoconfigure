package dev.luizveronesi.autoconfigure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfiguration {

	private static final String BEARER_SCHEME = "bearer";
	
	@Value("${app.documentation.name}")
	private String moduleName;
	
	@Value("${app.documentation.version}")
	private String apiVersion;
	
	@Bean
	public OpenAPI customOpenAPI() {
		String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));

		var info = new Info().title(apiTitle).version(apiVersion);
		var components = new Components()
							.addSecuritySchemes(BEARER_SCHEME, getBearerScheme());
		
		var api = new OpenAPI();
		api.addSecurityItem(new SecurityRequirement()).components(components).info(info);
		
		return api;
	}
	
	private SecurityScheme getBearerScheme() {
		return new SecurityScheme()
					.name(BEARER_SCHEME)
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT");
	}
}