package br.fiap.portal.repository;

import br.fiap.portal.model.PlanoCuidado;
import br.fiap.portal.model.StatusCuidado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanoCuidadoRepository extends JpaRepository<PlanoCuidado, Long> {
    Page<PlanoCuidado> findByTituloContainingIgnoreCaseOrPetNomeContainingIgnoreCase(
            String titulo,
            String petNome,
            Pageable pageable
    );

    Page<PlanoCuidado> findByStatus(StatusCuidado status, Pageable pageable);
}
