package dev.hiruna.rescuenet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}