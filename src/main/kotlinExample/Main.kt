package main.kotlinExample

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import main.PersonDeserializeException
import main.entity.Person

fun main(args: Array<String>) {
    val json1 = "{\"age\":23, \"name\":\"jack\"}"
    val json2 = "{\"age\":21, \"PersonName\":\"peter\"}"

    var objectMapper =  ObjectMapper().registerModule(
            SimpleModule().addDeserializer(
                    Person::class.java,
                    PersonDeserializer()
            )
    )

    println(objectMapper.readValue(json1, Person::class.java))
    println(objectMapper.readValue<Person>(json2, Person::class.java))
}

class PersonDeserializer : StdDeserializer<Person>(Person::class.java) {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): Person {
        var INVALID_NAME = "INVALID_NAME"
        var INVALID_AGE = -1
        val VALID_AGE_FIELD_NAME = listOf("age", "Age")
        val VALID_NAME_FIELD_NAME = listOf("name", "PersonName")
        var age: Int = INVALID_AGE
        var name: String = INVALID_NAME

        var jsonNode = jp.readValueAsTree<JsonNode>()
        jsonNode.fieldNames().forEach {
            if(VALID_AGE_FIELD_NAME.contains(it))
                age = jsonNode.get(it).intValue()
            else if(VALID_NAME_FIELD_NAME.contains(it))
                name = jsonNode.get(it).toString()
        }

        if(age == INVALID_AGE || name == INVALID_NAME)
            throw PersonDeserializeException("err json= ${jp.text}")

        return Person(age, name)
    }
}

