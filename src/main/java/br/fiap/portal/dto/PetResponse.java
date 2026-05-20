package br.fiap.portal.dto;

import br.fiap.portal.model.Pet;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PetResponse(
        Long id,
        String nome,
        String especie,
        String raca,
        LocalDate dataNascimento,
        BigDecimal pesoKg,
        Long tutorId,
        String tutorNome,
        long totalPlanos
) {
    public static PetResponse from(Pet pet) {
        long totalPlanos = pet.getPlanos() == null ? 0 : pet.getPlanos().size();
        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getEspecie(),
                pet.getRaca(),
                pet.getDataNascimento(),
                pet.getPesoKg(),
                pet.getTutor().getId(),
                pet.getTutor().getNome(),
                totalPlanos
        );
    }
}
