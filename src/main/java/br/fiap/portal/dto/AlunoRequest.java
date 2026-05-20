package br.fiap.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AlunoRequest(
        @NotBlank(message = "RM e obrigatorio")
        @Pattern(regexp = "^[A-Z0-9]{2,7}$", message = "RM deve ter de 2 a 7 letras ou numeros")
        String rmAluno,

        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 80, message = "Nome deve ter no maximo 80 caracteres")
        String nmAluno,

        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dtNascimento
) {
}
