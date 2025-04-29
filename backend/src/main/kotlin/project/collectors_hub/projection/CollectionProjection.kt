package project.collectors_hub.projection

interface CollectionProjection {
    fun getId(): Long
    fun getName(): String
    fun getDescription(): String
}