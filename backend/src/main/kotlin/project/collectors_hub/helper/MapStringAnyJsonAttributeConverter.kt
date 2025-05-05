package project.collectors_hub.helper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class MapStringAnyJsonAttributeConverter : AttributeConverter<Map<String, Any>, String> {

    private val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: Map<String, Any>?): String? {
        return attribute?.let {
            objectMapper.writeValueAsString(it)
        }
    }

    override fun convertToEntityAttribute(dbData: String?): Map<String, Any>? {
        return dbData?.let { objectMapper.readValue(it) }
    }

}