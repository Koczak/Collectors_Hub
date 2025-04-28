package project.collectors_hub.dto

interface CollectionProjection {
    fun getId(): Long
    fun getName(): String
    fun getDescription(): String
}