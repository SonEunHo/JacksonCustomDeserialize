package main.javaExample;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import main.entity.Person;

public class Main {

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Person.class, new PersonDeserializer());
            mapper.registerModule(module);

            String json1 = "{\"age\":11, \"name\":\"jack\"}";
            String json2 = "{\"age\":21, \"PersonName\":\"name\"}";
            Person p1 = mapper.readValue(json1, Person.class);
            Person p2 = mapper.readValue(json2, Person.class);
            System.out.println(p1);
            System.out.println(p2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class PersonDeserializer extends StdDeserializer<Person> {

    public PersonDeserializer() {
        this(null);
    }

    public PersonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Person deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        List<String> VALID_AGE_FIELD_NAME = Arrays.asList("age", "Age");
        List<String> VALID_NAME_FIELD_NAME = Arrays.asList("name", "PersonName");
        int age = -1;
        String name = "INVALID_NAME";

        JsonNode jsonNode = jp.getCodec().readTree(jp);
        Iterator<Entry<String, JsonNode>> iter = jsonNode.fields();
        while(iter.hasNext()) {
            Entry<String, JsonNode> field = iter.next();
            if(VALID_AGE_FIELD_NAME.contains(field.getKey()))
                age = field.getValue().asInt();
            else if(VALID_NAME_FIELD_NAME.contains(field.getKey()))
                name = field.getValue().toString();
        }

        return new Person(age, name);
    }
}
