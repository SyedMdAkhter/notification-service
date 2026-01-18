package io.github.syedmdakhter.notificationservice.dispatcher;

import io.github.syedmdakhter.notificationservice.channel.EmailNotificationChannel;
import io.github.syedmdakhter.notificationservice.channel.NotificationChannel;
import io.github.syedmdakhter.notificationservice.dto.Channel;
import io.github.syedmdakhter.notificationservice.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultNotificationDispatcher implements NotificationDispatcher{

    private static final Logger log =
            LoggerFactory.getLogger(EmailNotificationChannel.class);

    private final NotificationChannel emailChannel;

    public DefaultNotificationDispatcher(NotificationChannel emailChannel) {
        this.emailChannel = emailChannel;
    }

    @Override
    public void dispatch(NotificationRequest request) {

        log.info("Sending email in background thread: {}",
                Thread.currentThread().getName());

        if (request.getChannel() == Channel.EMAIL) {
            emailChannel.send(request);
            return;
        }

        throw new UnsupportedOperationException(
                "Unsupported notification channel: " + request.getChannel()
        );
    }

}
