package project.collectors_hub.controller.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import project.collectors_hub.service.UserService

//@Controller
//@RequestMapping("/users")
//class UserViewController(
//    private val userService: UserService
//) {
//
//    @GetMapping
//    fun viewUsers(model: Model): String {
//        val users = userService.getAllUsers()
//        model.addAttribute("users", users)
//        return "users"
//    }
//}