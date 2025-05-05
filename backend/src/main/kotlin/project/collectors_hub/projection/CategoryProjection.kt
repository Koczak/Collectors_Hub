package project.collectors_hub.projection

interface CategoryProjection {
    fun getId(): Long
    fun getName(): String
    fun getUsername(): String
    fun getAttributes(): Set<String>?
}