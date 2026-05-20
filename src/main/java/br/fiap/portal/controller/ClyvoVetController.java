package br.fiap.portal.controller;

import br.fiap.portal.dto.PetRequest;
import br.fiap.portal.dto.PetResponse;
import br.fiap.portal.dto.PlanoCuidadoRequest;
import br.fiap.portal.dto.PlanoCuidadoResponse;
import br.fiap.portal.dto.TutorRequest;
import br.fiap.portal.dto.TutorResponse;
import br.fiap.portal.model.StatusCuidado;
import br.fiap.portal.service.ClyvoVetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ClyvoVetController {
    private final ClyvoVetService service;

    public ClyvoVetController(ClyvoVetService service) {
        this.service = service;
    }

    @GetMapping("/tutores")
    public Page<TutorResponse> listarTutores(
            @RequestParam(required = false) String busca,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable
    ) {
        return service.listarTutores(busca, pageable);
    }

    @GetMapping("/tutores/{id}")
    public TutorResponse buscarTutor(@PathVariable Long id) {
        return service.buscarTutor(id);
    }

    @PostMapping("/tutores")
    public ResponseEntity<TutorResponse> criarTutor(@Valid @RequestBody TutorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarTutor(request));
    }

    @PutMapping("/tutores/{id}")
    public TutorResponse atualizarTutor(@PathVariable Long id, @Valid @RequestBody TutorRequest request) {
        return service.atualizarTutor(id, request);
    }

    @DeleteMapping("/tutores/{id}")
    public ResponseEntity<Void> excluirTutor(@PathVariable Long id) {
        service.excluirTutor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pets")
    public Page<PetResponse> listarPets(
            @RequestParam(required = false) String busca,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable
    ) {
        return service.listarPets(busca, pageable);
    }

    @GetMapping("/pets/{id}")
    public PetResponse buscarPet(@PathVariable Long id) {
        return service.buscarPet(id);
    }

    @PostMapping("/pets")
    public ResponseEntity<PetResponse> criarPet(@Valid @RequestBody PetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarPet(request));
    }

    @PutMapping("/pets/{id}")
    public PetResponse atualizarPet(@PathVariable Long id, @Valid @RequestBody PetRequest request) {
        return service.atualizarPet(id, request);
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> excluirPet(@PathVariable Long id) {
        service.excluirPet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/planos-cuidado")
    public Page<PlanoCuidadoResponse> listarPlanos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusCuidado status,
            @PageableDefault(size = 10, sort = "dataPrevista") Pageable pageable
    ) {
        return service.listarPlanos(busca, status, pageable);
    }

    @GetMapping("/planos-cuidado/{id}")
    public PlanoCuidadoResponse buscarPlano(@PathVariable Long id) {
        return service.buscarPlano(id);
    }

    @PostMapping("/planos-cuidado")
    public ResponseEntity<PlanoCuidadoResponse> criarPlano(@Valid @RequestBody PlanoCuidadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarPlano(request));
    }

    @PutMapping("/planos-cuidado/{id}")
    public PlanoCuidadoResponse atualizarPlano(@PathVariable Long id, @Valid @RequestBody PlanoCuidadoRequest request) {
        return service.atualizarPlano(id, request);
    }

    @DeleteMapping("/planos-cuidado/{id}")
    public ResponseEntity<Void> excluirPlano(@PathVariable Long id) {
        service.excluirPlano(id);
        return ResponseEntity.noContent().build();
    }
}
