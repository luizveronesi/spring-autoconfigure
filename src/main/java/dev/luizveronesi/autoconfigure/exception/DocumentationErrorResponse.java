package dev.luizveronesi.autoconfigure.exception;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Documentation error response is a wrapper used for OpenAPI interface
 * documentations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentationErrorResponse implements Serializable {

    private String description;

    private List<String> details;

}
