package project.backend.global.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateText = jsonParser.getText();
        try {
            return LocalDateTime.parse(dateText, dateTimeFormatter);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse date: " + dateText, e);
        }
    }
}