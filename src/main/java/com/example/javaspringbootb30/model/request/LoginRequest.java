package com.example.javaspringbootb30.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotEmpty(message = "Email không được để trống")
    @Email(message="Email không đúng định dạng")
    String email;
    @NotEmpty(message = "Mật khẩu không được để trống")
    @Length(min=3,message="Mật khẩu phải có ít nhất 3 kí tự")
    String password;
}
