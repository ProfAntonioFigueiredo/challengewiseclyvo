package br.fiap.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TutorRequest(
        @NotBlank(message = "Nome do tutor e obrigatorio")
        @Size(max = 120, message = "Nome do tutor deve ter no maximo 120 caracteres")
        String nome,

        @NotBlank(message = "E-mail e obrigatorio")
        @Email(message = "E-mail invalido")
        @Size(max = 120, message = "E-mail deve ter no maximo 120 caracteres")
        String email,

        @NotBlank(message = "Telefone e obrigatorio")
        @Pattern(regexp = "^[0-9+()\\-\\s]{8,20}$", message = "Telefone invalido")
        String telefone
) {
}
