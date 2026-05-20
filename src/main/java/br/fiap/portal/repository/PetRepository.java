package br.fiap.portal.repository;

import br.fiap.portal.model.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Page<Pet> findByNomeContainingIgnoreCaseOrEspecieContainingIgnoreCaseOrTutorNomeContainingIgnoreCase(
            String nome,
            String especie,
            String tutorNome,
            Pageable pageable
    );
}
