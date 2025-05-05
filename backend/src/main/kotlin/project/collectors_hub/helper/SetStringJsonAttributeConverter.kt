package project.collectors_hub.helper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class SetStringJsonAttributeConverter : AttributeConverter<Set<String>, String> {
    private val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: Set<String>?): String? {
        return attribute?.let { objectMapper.writeValueAsString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): Set<String>? {
        return dbData?.let {
            objectMapper.readValue(it, object : TypeReference<Set<String>>() {})
        }
    }
}