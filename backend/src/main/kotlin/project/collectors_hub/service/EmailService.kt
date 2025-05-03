package project.collectors_hub.service

interface EmailService {
    fun sendEmail(to: String, subject: String, text: String)
}