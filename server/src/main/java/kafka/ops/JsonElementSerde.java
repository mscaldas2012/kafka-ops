package kafka.ops;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

import java.util.Map;


public class JsonElementSerde extends Serdes.WrapperSerde<JsonElement> {
    public JsonElementSerde() {
        super(new Serializer<JsonElement>() {
            private Gson gson = new Gson();

            @Override
            public void configure(Map<String, ?> map, boolean b) {
            }

            @Override
            public byte[] serialize(String topic, JsonElement data) {
                return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public void close() {
            }
        }, new Deserializer<JsonElement>() {
            @Override
            public void configure(Map<String, ?> configs, boolean isKey) {

            }

            @Override
            public JsonElement deserialize(String topic, byte[] data) {

                return JsonParser.parseString(new String(data));
                //return gson.fromJson(new String(data), ReportSummary.class);
            }

            @Override
            public void close() {

            }
        });
    }
}
