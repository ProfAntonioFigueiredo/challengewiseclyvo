package br.fiap.portal.repository;

import br.fiap.portal.model.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<Tutor> findByEmailIgnoreCase(String email);

    Page<Tutor> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nome,
            String email,
            Pageable pageable
    );
}
