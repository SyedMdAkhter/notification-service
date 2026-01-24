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
            // Send admin notification (existing behavior)
            sendAdminEmail(request);

            // Send customer acknowledgement (new)
            sendAcknowledgementEmail(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAdminEmail(NotificationRequest request) {

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

    private void sendAcknowledgementEmail(NotificationRequest request) throws Exception {

        Map<String, Object> data = request.getData();

        // Safety check: if customer email is missing, skip acknowledgement
        if (data == null || !data.containsKey("email")) {
            return;
        }

        String customerEmail = data.get("email").toString();

        String body = processTemplate(
                "acknowledgement-email",
                data
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(customerEmail);
        message.setSubject("We have received your query");
        message.setText(body);

        mailSender.send(message);
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
