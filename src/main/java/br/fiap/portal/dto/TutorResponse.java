package br.fiap.portal.dto;

import br.fiap.portal.model.Tutor;

public record TutorResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        long totalPets
) {
    public static TutorResponse from(Tutor tutor) {
        long totalPets = tutor.getPets() == null ? 0 : tutor.getPets().size();
        return new TutorResponse(tutor.getId(), tutor.getNome(), tutor.getEmail(), tutor.getTelefone(), totalPets);
    }
}
