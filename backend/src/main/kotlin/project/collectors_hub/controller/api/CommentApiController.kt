package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.collectors_hub.dto.CommentDto
import project.collectors_hub.projection.CommentProjection
import project.collectors_hub.service.CommentService

@RestController
@RequestMapping(value = ["/api/comments"])
class CommentApiController(
    private val commentService: CommentService
) {

    @GetMapping("/item/{itemId}")
    fun getCommentsForItem(@PathVariable itemId: Long): ResponseEntity<List<CommentProjection>> {
        return ResponseEntity(commentService.getAllCommentsForItem(itemId), HttpStatus.OK)
    }

    @GetMapping("/collection/{collectionId}")
    fun getCommentsForCollection(@PathVariable collectionId: Long): ResponseEntity<List<CommentProjection>> {
        return ResponseEntity(commentService.getAllCommentsForCollection(collectionId), HttpStatus.OK)
    }

    @PostMapping
    fun addComment(@RequestBody dto: CommentDto): ResponseEntity<Long> {
        return ResponseEntity(commentService.addComment(dto), HttpStatus.CREATED)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Unit> {
        commentService.deleteComment(commentId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
} 