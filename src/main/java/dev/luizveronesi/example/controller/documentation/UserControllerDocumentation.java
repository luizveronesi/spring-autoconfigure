package dev.luizveronesi.example.controller.documentation;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import dev.luizveronesi.autoconfigure.configuration.OpenApiConfiguration;
import dev.luizveronesi.autoconfigure.exception.DocumentationErrorResponse;
import dev.luizveronesi.autoconfigure.security.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

public interface UserControllerDocumentation {

    @Operation(security = @SecurityRequirement(name = OpenApiConfiguration.OAUTH_SCHEME))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieves logged user", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DocumentationErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DocumentationErrorResponse.class))
            })
    })
    ResponseEntity<UserProfile> getLogged();
}