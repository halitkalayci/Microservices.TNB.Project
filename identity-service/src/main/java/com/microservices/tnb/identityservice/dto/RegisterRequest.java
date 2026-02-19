package com.microservices.tnb.identityservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Kullanıcı adı boş olamaz")
        @Size(min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter arasında olmalıdır")
        String username,

        @NotBlank(message = "E-posta adresi boş olamaz")
        @Email(message = "Geçerli bir e-posta adresi giriniz")
        String email,

        @NotBlank(message = "Şifre boş olamaz")
        @Size(min = 8, max = 100, message = "Şifre en az 8 karakter olmalıdır")
        String password
) {
}
