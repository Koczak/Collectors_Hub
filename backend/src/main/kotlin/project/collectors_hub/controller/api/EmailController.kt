package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<String> {
        emailService.sendEmail(to, subject, text)
        return ResponseEntity("Email sent successfully to $to", HttpStatus.OK)
    }

}