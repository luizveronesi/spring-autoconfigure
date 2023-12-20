package dev.luizveronesi.autoconfigure.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.luizveronesi.autoconfigure.serializers.LocalDateDeserializer;
import dev.luizveronesi.autoconfigure.serializers.LocalDateSerializer;
import dev.luizveronesi.autoconfigure.serializers.LocalDateTimeDeserializer;
import dev.luizveronesi.autoconfigure.serializers.LocalDateTimeSerializer;
import dev.luizveronesi.autoconfigure.serializers.LocalTimeDeserializer;
import dev.luizveronesi.autoconfigure.serializers.LocalTimeSerializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "spring.jackson", name = "override", havingValue = "false", matchIfMissing = true)
public class ObjectMapperAutoConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
            List<JsonSerializer<?>> serializers, List<JsonDeserializer<?>> deserializers) {
        return builder -> builder.serializers(serializers.toArray(new JsonSerializer[0]))
                .deserializers(deserializers.toArray(new JsonDeserializer[0]))
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Bean
    public LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer();
    }

    @Bean
    public LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer();
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer();
    }

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer();
    }

    @Bean
    public LocalTimeDeserializer localTimeDeserializer() {
        return new LocalTimeDeserializer();
    }

    @Bean
    public LocalTimeSerializer localTimeSerializer() {
        return new LocalTimeSerializer();
    }

    public static ObjectMapper mapper() {
        return new ObjectMapper()
                .setSerializationInclusion(Include.NON_NULL)
                .setSerializationInclusion(Include.NON_EMPTY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new SimpleModule()
                        .addSerializer(LocalDate.class, new LocalDateSerializer())
                        .addSerializer(LocalTime.class, new LocalTimeSerializer())
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                        .addDeserializer(LocalDate.class, new LocalDateDeserializer())
                        .addDeserializer(LocalTime.class, new LocalTimeDeserializer())
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()));
    }
}
