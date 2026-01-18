package io.github.syedmdakhter.notificationservice.controller;

import io.github.syedmdakhter.notificationservice.dispatcher.NotificationDispatcher;
import io.github.syedmdakhter.notificationservice.dto.NotificationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationDispatcher dispatcher;

    public NotificationController(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping
    public ResponseEntity<?> sendNotification(@Valid @RequestBody NotificationRequest request){

        dispatcher.dispatch(request);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Your message has been submitted successfully.");
    }

}
