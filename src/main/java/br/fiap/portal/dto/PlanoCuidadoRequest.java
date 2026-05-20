package br.fiap.portal.dto;

import br.fiap.portal.model.CategoriaCuidado;
import br.fiap.portal.model.StatusCuidado;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PlanoCuidadoRequest(
        @NotNull(message = "Pet e obrigatorio")
        Long petId,

        @NotBlank(message = "Titulo e obrigatorio")
        @Size(max = 120, message = "Titulo deve ter no maximo 120 caracteres")
        String titulo,

        @NotNull(message = "Categoria e obrigatoria")
        CategoriaCuidado categoria,

        StatusCuidado status,

        @NotNull(message = "Data prevista e obrigatoria")
        @FutureOrPresent(message = "Data prevista deve ser hoje ou futura")
        LocalDate dataPrevista,

        @Size(max = 500, message = "Observacao deve ter no maximo 500 caracteres")
        String observacao
) {
}
