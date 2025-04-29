package project.collectors_hub.controller.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import project.collectors_hub.dto.UserForm
import project.collectors_hub.service.UserService

//@Controller
//@RequestMapping("/register")
//class RegistrationController(
//    private val userService: UserService
//) {
//
//    @GetMapping
//    fun showRegistrationForm(model: Model): String {
//        model.addAttribute("user", UserForm())
//        return "register"
//    }
//
//    @PostMapping
//    fun registerUser(userForm: UserForm): String {
//        userService.createUser(userForm)
//        return "redirect:/login"
//    }
//}