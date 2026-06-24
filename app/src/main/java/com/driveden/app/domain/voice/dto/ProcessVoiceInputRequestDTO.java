package com.driveden.app.domain.voice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessVoiceInputRequestDTO {

    @NotBlank(message = "text is required")
    @Size(max = 1000, message = "text must be at most 1000 characters")
    private String text;
}
