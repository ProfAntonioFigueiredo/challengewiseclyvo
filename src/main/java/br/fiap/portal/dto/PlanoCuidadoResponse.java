package br.fiap.portal.dto;

import br.fiap.portal.model.CategoriaCuidado;
import br.fiap.portal.model.PlanoCuidado;
import br.fiap.portal.model.StatusCuidado;

import java.time.LocalDate;

public record PlanoCuidadoResponse(
        Long id,
        Long petId,
        String petNome,
        String tutorNome,
        String titulo,
        CategoriaCuidado categoria,
        StatusCuidado status,
        LocalDate dataPrevista,
        String observacao
) {
    public static PlanoCuidadoResponse from(PlanoCuidado plano) {
        return new PlanoCuidadoResponse(
                plano.getId(),
                plano.getPet().getId(),
                plano.getPet().getNome(),
                plano.getPet().getTutor().getNome(),
                plano.getTitulo(),
                plano.getCategoria(),
                plano.getStatus(),
                plano.getDataPrevista(),
                plano.getObservacao()
        );
    }
}
