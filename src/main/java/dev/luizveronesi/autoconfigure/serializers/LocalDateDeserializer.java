package dev.luizveronesi.autoconfigure.serializers;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import dev.luizveronesi.autoconfigure.utils.DateUtil;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return DateUtil.toDate(jsonParser.getValueAsString());
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

}
