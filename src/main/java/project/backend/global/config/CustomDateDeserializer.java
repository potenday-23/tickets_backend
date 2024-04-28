package project.backend.global.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CustomDateDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateText = jsonParser.getText();
        try {
            return dateFormat.parse(dateText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse date: " + dateText, e);
        }
    }
}