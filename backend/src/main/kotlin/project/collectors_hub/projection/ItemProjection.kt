package project.collectors_hub.projection

interface ItemProjection {
    fun getId(): Long
    fun getName(): String
    fun getDescription(): String
    fun getCategoryName(): String?
    fun getAttributes(): Map<String, Any>?
    fun getCollectionId(): Long
}