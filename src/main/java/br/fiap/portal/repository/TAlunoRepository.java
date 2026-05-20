package br.fiap.portal.repository;

import br.fiap.portal.model.TAluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TAlunoRepository extends JpaRepository<TAluno, String> {
    Page<TAluno> findByRmAlunoContainingIgnoreCaseOrNmAlunoContainingIgnoreCase(
            String rmAluno,
            String nmAluno,
            Pageable pageable
    );
}
