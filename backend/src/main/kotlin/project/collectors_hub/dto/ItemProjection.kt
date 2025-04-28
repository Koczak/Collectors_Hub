package project.collectors_hub.dto

interface ItemProjection {
    fun getId(): Long
    fun getName(): String
    fun getDescription(): String
    fun getCategoryName(): String?
}