package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import project.collectors_hub.service.EmailService

@RestController
@RequestMapping("/api/email")
class EmailController(
    private val emailService: EmailService
) {

    @PostMapping("/send")
    fun sendEmail(
        @RequestParam to: String,
        @RequestParam subject: String,
        @RequestParam text: String
    ): String {
        emailService.sendEmail(to, subject, text)
        return "Email sent successfully to $to"
    }

}