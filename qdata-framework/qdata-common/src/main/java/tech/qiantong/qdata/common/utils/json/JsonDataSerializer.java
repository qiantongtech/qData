package tech.qiantong.qdata.common.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-20 09:34
 **/
public class JsonDataSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeRawValue(value);
    }
}
