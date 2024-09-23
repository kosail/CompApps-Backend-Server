package com.korealm.compApp.services

import com.korealm.compApp.services.enums.EmailReason
import org.springframework.stereotype.Service
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Async
import java.nio.file.Files
import java.nio.file.Paths

@Service
class EmailService (private val mailSender: JavaMailSender) {

    private fun loadTemplate(templateName: EmailReason): String {
        val resource: Resource = ClassPathResource(templateName.getPath())
        return Files.readString(Paths.get(resource.uri))
    }

    private fun sendEmail(to: String, subject: String, htmlContent: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        // setFrom was used because for some reason, in the application.properties file the field for email setting was not working, even tho it was being recognized.
        // Still, the email here should match the one in applications.properties.
        helper.setFrom("email@example.com", "Equipo de CompApp")

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)
        mailSender.send(message)
    }

    @Async
    fun sendWelcomeMessage(recipient: String, name: String) {
        val subject = "¡Bienvenido a CompApp!"
        val template: String = loadTemplate(EmailReason.WELCOME)
        val tailoredMessage = template.replace("burrotote", name)

        sendEmail(recipient, subject, tailoredMessage)
    }

    @Async
    fun sendPasswordReset(recipient:String, newPassword: String) {
        val subject = "Solicitud de cambio de contraseña"
        val template: String = loadTemplate(EmailReason.PASSWORD_RESET)
        val tailoredMessage = template.replace("ULTRASECRETO", newPassword)

        sendEmail(recipient, subject, tailoredMessage)
    }
}