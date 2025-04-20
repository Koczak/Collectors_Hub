package project.collectors_hub.controller.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import project.collectors_hub.dto.CollectionForm
import project.collectors_hub.service.CollectionService

@Controller
@RequestMapping("/collections")
class CollectionViewController(
    private val collectionService: CollectionService
) {

    @GetMapping
    fun showCollections(model: Model): String {
        val collections = collectionService.getAllCollectionsForCurrentUser()
        model.addAttribute("collections", collections)
        return "collections"
    }

    @GetMapping("/new")
    fun showCreateCollectionForm(model: Model): String {
        model.addAttribute("collectionForm", CollectionForm())
        return "create_collection"
    }

    @PostMapping
    fun createCollection(collectionForm: CollectionForm): String {
        collectionService.createCollection(collectionForm)
        return "redirect:/collections"
    }
}