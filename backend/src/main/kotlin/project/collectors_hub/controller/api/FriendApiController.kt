package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.collectors_hub.service.FriendService

@RestController
@RequestMapping(value = ["/api/friends"])
class FriendApiController(
    private val friendService: FriendService
) {

    @GetMapping("/confirm/{invitationId}")
    fun confirmFriendRequest(@PathVariable invitationId: Long): ResponseEntity<Unit> {
        friendService.confirmFriendRequest(invitationId)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @PostMapping("/invite/{email}")
    fun sendFriendRequest(@PathVariable email: String): ResponseEntity<Unit> {
        friendService.sendFriendRequest(email)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/reject/{invitationId}")
    fun rejectFriendRequest(@PathVariable invitationId: Long): ResponseEntity<Unit> {
        friendService.rejectFriendRequest(invitationId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}