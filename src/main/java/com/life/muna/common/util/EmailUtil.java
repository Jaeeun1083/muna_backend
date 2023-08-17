package com.life.muna.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

@Component
public class EmailUtil {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final String host;
    private final int port;
    private final boolean auth;
    private final boolean starttls;
    private final String username;
    private final String password;

    public EmailUtil(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.properties.mail.smtp.auth}") boolean auth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable}") boolean starttls,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password
    ) {
        this.host=host;
        this.port=port;
        this.auth=auth;
        this.starttls=starttls;
        this.username=username;
        this.password=password;
    }

    public void sendHtmlEmail(String email, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8"); // HTML 내용을 설정

            Transport.send(message);

            LOG.info("Email sent successfully.");
        } catch (MessagingException e) {
            LOG.info("Failed to send email. Error message: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public String readResourceFile(String filePath) {
        try {
            File file = ResourceUtils.getFile(filePath);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
