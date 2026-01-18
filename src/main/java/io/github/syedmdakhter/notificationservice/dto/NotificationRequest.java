package io.github.syedmdakhter.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NotificationRequest {
    @NotNull
    private Channel channel;

    @NotEmpty
    private List<String> to;

    private List<String> cc;

    @NotBlank
    private String subject;

    @NotBlank
    private String template;

    @NotNull
    private Map<String, Object> data;
}
