package br.fiap.portal.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PetRequest(
        @NotBlank(message = "Nome do pet e obrigatorio")
        @Size(max = 80, message = "Nome do pet deve ter no maximo 80 caracteres")
        String nome,

        @NotBlank(message = "Especie e obrigatoria")
        @Size(max = 40, message = "Especie deve ter no maximo 40 caracteres")
        String especie,

        @Size(max = 60, message = "Raca deve ter no maximo 60 caracteres")
        String raca,

        @PastOrPresent(message = "Data de nascimento nao pode estar no futuro")
        LocalDate dataNascimento,

        @DecimalMin(value = "0.01", message = "Peso deve ser maior que zero")
        BigDecimal pesoKg,

        @NotNull(message = "Tutor e obrigatorio")
        Long tutorId
) {
}
