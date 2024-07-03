package com.example.javaspringbootb30.model.request;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotEmpty(message = "Tên người dùng không được để trống")
    String name;
    @Column(unique = true,nullable = false)
    @Email(message="Email không đúng định dạng")
    String email;
    @NotEmpty(message = "Mật khẩu không được để trống")
    @Length(min=3,message="Mật khẩu phải có ít nhất 3 kí tự")
    String password;
}