package dev.hiruna.rescuenet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private int code;        // HTTP status code
    private String description; // Description of the error
}