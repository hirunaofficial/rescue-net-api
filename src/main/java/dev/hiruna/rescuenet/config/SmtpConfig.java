package dev.hiruna.rescuenet.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SmtpConfig {

    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Load SMTP details from .env file
        String host = dotenv.get("SMTP_HOST");
        int port = Integer.parseInt(dotenv.get("SMTP_PORT"));
        String username = dotenv.get("SMTP_USERNAME");
        String password = dotenv.get("SMTP_PASSWORD");
        boolean auth = Boolean.parseBoolean(dotenv.get("SMTP_AUTH"));

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        return mailSender;
    }
}