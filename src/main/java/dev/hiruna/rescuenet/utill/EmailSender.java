package dev.hiruna.rescuenet.utill;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;
    private final Dotenv dotenv = Dotenv.load();

    public void sendEmail(String to, String subject, String body) {
        try {
            // Create a new MIME message
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set email details
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(dotenv.get("SMTP_FROM"));

            // Send the email
            mailSender.send(mimeMessage);

        } catch (MailException | MessagingException e) {
            // Log and rethrow runtime exception if email fails
            throw new RuntimeException("Failed to send email", e);
        }
    }
}