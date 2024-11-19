package dev.hiruna.rescuenet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    private String status;  // "success" or "error"
    private String message; // General message describing the status
    private T data;         // Data (if any) associated with the response
    private ErrorDTO error; // Error details (if any)

    // Constructor to easily create an error response
    public static <T> ResponseDTO<T> error(String message, int code, String description) {
        return new ResponseDTO<>("error", message, null, new ErrorDTO(code, description));
    }

    // Constructor to easily create a success response
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>("success", message, data, null);
    }
}