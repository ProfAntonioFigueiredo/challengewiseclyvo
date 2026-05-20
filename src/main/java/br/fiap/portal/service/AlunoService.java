package br.fiap.portal.service;

import br.fiap.portal.dto.AlunoRequest;
import br.fiap.portal.dto.AlunoResponse;
import br.fiap.portal.model.TAluno;
import br.fiap.portal.repository.TAlunoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AlunoService {
    private final TAlunoRepository repository;

    public AlunoService(TAlunoRepository repository) {
        this.repository = repository;
    }

    @Cacheable("alunos")
    public Page<AlunoResponse> listar(String busca, Pageable pageable) {
        Page<TAluno> page;

        if (busca == null || busca.isBlank()) {
            page = repository.findAll(pageable);
        } else {
            String term = busca.trim();
            page = repository.findByRmAlunoContainingIgnoreCaseOrNmAlunoContainingIgnoreCase(term, term, pageable);
        }

        return page.map(AlunoResponse::from);
    }

    @Cacheable(value = "alunoPorRm", key = "#rmAluno")
    public AlunoResponse buscarPorRm(String rmAluno) {
        return repository.findById(rmAluno)
                .map(AlunoResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno nao encontrado."));
    }

    @CacheEvict(value = {"alunos", "alunoPorRm"}, allEntries = true)
    @Transactional
    public AlunoResponse criar(AlunoRequest request) {
        String rm = normalizeRm(request.rmAluno());

        if (repository.existsById(rm)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe aluno com este RM.");
        }

        TAluno aluno = toEntity(request);
        aluno.setRmAluno(rm);
        return AlunoResponse.from(repository.save(aluno));
    }

    @CacheEvict(value = {"alunos", "alunoPorRm"}, allEntries = true)
    @Transactional
    public AlunoResponse atualizar(String rmAluno, AlunoRequest request) {
        String rm = normalizeRm(rmAluno);

        if (!repository.existsById(rm)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno nao encontrado.");
        }

        TAluno aluno = toEntity(request);
        aluno.setRmAluno(rm);
        return AlunoResponse.from(repository.save(aluno));
    }

    @CacheEvict(value = {"alunos", "alunoPorRm"}, allEntries = true)
    @Transactional
    public void excluir(String rmAluno) {
        String rm = normalizeRm(rmAluno);

        if (!repository.existsById(rm)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno nao encontrado.");
        }

        repository.deleteById(rm);
    }

    private TAluno toEntity(AlunoRequest request) {
        TAluno aluno = new TAluno();
        aluno.setRmAluno(normalizeRm(request.rmAluno()));
        aluno.setNmAluno(request.nmAluno().trim());
        aluno.setDtNascimento(request.dtNascimento());
        return aluno;
    }

    private String normalizeRm(String rmAluno) {
        if (rmAluno == null) {
            return "";
        }
        return rmAluno.trim().toUpperCase();
    }
}
