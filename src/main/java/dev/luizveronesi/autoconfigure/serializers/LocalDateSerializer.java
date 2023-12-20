package dev.luizveronesi.autoconfigure.serializers;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import dev.luizveronesi.autoconfigure.utils.DateUtil;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (localDate == null) {
            jsonGenerator.writeString("");
        } else {
            jsonGenerator.writeString(DateUtil.toString(localDate));
        }
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

}
