package io.github.syedmdakhter.notificationservice.channel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.github.syedmdakhter.notificationservice.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

@Component
public class EmailNotificationChannel implements NotificationChannel{

    private final JavaMailSender mailSender;

    @Value("${notification.email.from}")
    private String from;

    private final Configuration freemarkerConfig;

    public EmailNotificationChannel(JavaMailSender mailSender,
                                    Configuration freemarkerConfig) {
        this.mailSender = mailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    @Async
    @Override
    public void send(NotificationRequest request) {

        try {
            String body = processTemplate(
                    request.getTemplate(),
                    request.getData()
            );

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(request.getTo().toArray(new String[0]));
            message.setSubject(request.getSubject());
            message.setText(body);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    private String processTemplate(String templateName,
                                   Map<String, Object> data) throws Exception {

        Template template =
                freemarkerConfig.getTemplate(templateName + ".ftl");

        StringWriter writer = new StringWriter();
        template.process(data, writer);

        return writer.toString();
    }


}
