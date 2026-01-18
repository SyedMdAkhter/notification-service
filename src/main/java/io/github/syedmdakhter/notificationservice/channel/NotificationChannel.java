package io.github.syedmdakhter.notificationservice.channel;

import io.github.syedmdakhter.notificationservice.dto.NotificationRequest;

public interface NotificationChannel {

    void send(NotificationRequest request);
}
