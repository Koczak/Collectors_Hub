package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.projection.UserFriendProjection
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

    @PostMapping("/invite/{username}")
    fun sendFriendRequest(@PathVariable username: String): ResponseEntity<Unit> {
        friendService.sendFriendRequest(username)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/reject/{invitationId}")
    fun rejectFriendRequest(@PathVariable invitationId: Long): ResponseEntity<Unit> {
        friendService.rejectFriendRequest(invitationId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping
    fun getAllFriendsForCurrentUser(): ResponseEntity<List<UserFriendProjection>> {
        return ResponseEntity(friendService.getAllFriendsForCurrentUser(), HttpStatus.OK)
    }

    @GetMapping("/collections/{username}")
    fun getAllCollectionsOfGivenFriend(@PathVariable username: String): ResponseEntity<List<CollectionProjection>> {
        return ResponseEntity(friendService.getAllCollectionsForFriend(username), HttpStatus.OK)
    }
}