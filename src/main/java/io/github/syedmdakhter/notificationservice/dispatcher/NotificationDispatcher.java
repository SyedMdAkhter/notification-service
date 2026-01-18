package io.github.syedmdakhter.notificationservice.dispatcher;

import io.github.syedmdakhter.notificationservice.dto.NotificationRequest;

public interface NotificationDispatcher {

    void dispatch(NotificationRequest request);
}
