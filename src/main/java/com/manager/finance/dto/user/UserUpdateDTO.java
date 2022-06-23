package com.manager.finance.dto.user;

import com.manager.finance.dto.CrudDTO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;

import static com.manager.finance.dto.user.UserDTO.EMAIL_REGEXP;

@Data
public class UserUpdateDTO implements CrudDTO {
    private String username;
    @ToString.Exclude
    private String password;
    @Email(message = "email", regexp = EMAIL_REGEXP)
    private String email;
    private String phone;

}
