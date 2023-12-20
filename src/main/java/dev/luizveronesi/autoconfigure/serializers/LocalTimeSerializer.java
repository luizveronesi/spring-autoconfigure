package dev.luizveronesi.autoconfigure.serializers;

import java.io.IOException;
import java.time.LocalTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import dev.luizveronesi.autoconfigure.utils.DateUtil;

public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    @Override
    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (localTime == null) {
            jsonGenerator.writeString("");
        } else {
            jsonGenerator.writeString(DateUtil.toString(localTime));
        }
    }

    @Override
    public Class<LocalTime> handledType() {
        return LocalTime.class;
    }

}
