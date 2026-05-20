package br.fiap.portal.controller;

import br.fiap.portal.dto.AlunoRequest;
import br.fiap.portal.dto.AlunoResponse;
import br.fiap.portal.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class TAlunoController {
    private final AlunoService service;

    public TAlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AlunoResponse> getAll(
            @RequestParam(required = false) String busca,
            @PageableDefault(size = 10, sort = "nmAluno") Pageable pageable
    ) {
        return service.listar(busca, pageable);
    }

    @GetMapping("/{rmAluno}")
    public AlunoResponse getById(@PathVariable String rmAluno) {
        return service.buscarPorRm(rmAluno);
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> create(@Valid @RequestBody AlunoRequest aluno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(aluno));
    }

    @PutMapping("/{rmAluno}")
    public AlunoResponse update(@PathVariable String rmAluno, @Valid @RequestBody AlunoRequest aluno) {
        return service.atualizar(rmAluno, aluno);
    }

    @DeleteMapping("/{rmAluno}")
    public ResponseEntity<Void> delete(@PathVariable String rmAluno) {
        service.excluir(rmAluno);
        return ResponseEntity.noContent().build();
    }
}
