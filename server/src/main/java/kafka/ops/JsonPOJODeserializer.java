package kafka.ops;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonPOJODeserializer<T> implements Deserializer<T> {
    private Logger logger = LoggerFactory.getLogger(JsonPOJODeserializer.class);
    ObjectMapper mapper  = new ObjectMapper();

    /**
     * Default constructor needed by Kafka
     */
    public JsonPOJODeserializer() {
    }

    @SuppressWarnings("unchecked")
//    @Override
//    public void configure(Map<String, ?> props, boolean isKey) {
//        tClass = (Class<T>) props.get("JsonPOJOClass");
//    }

    private String badJson = "{\"message\": \"Invalid JSON!\"}";
    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null)
            return null;
        T data;
        try {
            data = (T) mapper.readTree(new String(bytes));
        } catch (JsonProcessingException e) {
            logger.error("Unable to parse json: " + e);
           // throw new SerializationException(e);
            ObjectNode node = mapper.createObjectNode();
            node.put("INVALID", "Invalid Json: " + e);
            data = (T) node;
        }

        return data;
    }

    @Override
    public void close() {

    }
}