package br.fiap.portal.service;

import br.fiap.portal.dto.PetRequest;
import br.fiap.portal.dto.PetResponse;
import br.fiap.portal.dto.PlanoCuidadoRequest;
import br.fiap.portal.dto.PlanoCuidadoResponse;
import br.fiap.portal.dto.TutorRequest;
import br.fiap.portal.dto.TutorResponse;
import br.fiap.portal.model.Pet;
import br.fiap.portal.model.PlanoCuidado;
import br.fiap.portal.model.StatusCuidado;
import br.fiap.portal.model.Tutor;
import br.fiap.portal.repository.PetRepository;
import br.fiap.portal.repository.PlanoCuidadoRepository;
import br.fiap.portal.repository.TutorRepository;
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
public class ClyvoVetService {
    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;
    private final PlanoCuidadoRepository planoRepository;

    public ClyvoVetService(
            TutorRepository tutorRepository,
            PetRepository petRepository,
            PlanoCuidadoRepository planoRepository
    ) {
        this.tutorRepository = tutorRepository;
        this.petRepository = petRepository;
        this.planoRepository = planoRepository;
    }

    @Cacheable("tutores")
    public Page<TutorResponse> listarTutores(String busca, Pageable pageable) {
        Page<Tutor> page;

        if (busca == null || busca.isBlank()) {
            page = tutorRepository.findAll(pageable);
        } else {
            String term = busca.trim();
            page = tutorRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term, pageable);
        }

        return page.map(TutorResponse::from);
    }

    @Cacheable(value = "tutorPorId", key = "#id")
    public TutorResponse buscarTutor(Long id) {
        return TutorResponse.from(findTutor(id));
    }

    @CacheEvict(value = {"tutores", "tutorPorId", "pets", "planos"}, allEntries = true)
    @Transactional
    public TutorResponse criarTutor(TutorRequest request) {
        if (tutorRepository.existsByEmailIgnoreCase(request.email().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe tutor com este e-mail.");
        }

        Tutor tutor = new Tutor();
        tutor.setNome(request.nome().trim());
        tutor.setEmail(request.email().trim().toLowerCase());
        tutor.setTelefone(request.telefone().trim());
        return TutorResponse.from(tutorRepository.save(tutor));
    }

    @CacheEvict(value = {"tutores", "tutorPorId", "pets", "planos"}, allEntries = true)
    @Transactional
    public TutorResponse atualizarTutor(Long id, TutorRequest request) {
        Tutor tutor = findTutor(id);
        tutorRepository.findByEmailIgnoreCase(request.email().trim())
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe tutor com este e-mail.");
                });

        tutor.setNome(request.nome().trim());
        tutor.setEmail(request.email().trim().toLowerCase());
        tutor.setTelefone(request.telefone().trim());
        return TutorResponse.from(tutorRepository.save(tutor));
    }

    @CacheEvict(value = {"tutores", "tutorPorId", "pets", "planos"}, allEntries = true)
    @Transactional
    public void excluirTutor(Long id) {
        Tutor tutor = findTutor(id);
        tutorRepository.delete(tutor);
    }

    @Cacheable("pets")
    public Page<PetResponse> listarPets(String busca, Pageable pageable) {
        Page<Pet> page;

        if (busca == null || busca.isBlank()) {
            page = petRepository.findAll(pageable);
        } else {
            String term = busca.trim();
            page = petRepository.findByNomeContainingIgnoreCaseOrEspecieContainingIgnoreCaseOrTutorNomeContainingIgnoreCase(
                    term,
                    term,
                    term,
                    pageable
            );
        }

        return page.map(PetResponse::from);
    }

    @Cacheable(value = "petPorId", key = "#id")
    public PetResponse buscarPet(Long id) {
        return PetResponse.from(findPet(id));
    }

    @CacheEvict(value = {"pets", "petPorId", "planos", "tutores", "tutorPorId"}, allEntries = true)
    @Transactional
    public PetResponse criarPet(PetRequest request) {
        Pet pet = new Pet();
        applyPetRequest(pet, request);
        return PetResponse.from(petRepository.save(pet));
    }

    @CacheEvict(value = {"pets", "petPorId", "planos", "tutores", "tutorPorId"}, allEntries = true)
    @Transactional
    public PetResponse atualizarPet(Long id, PetRequest request) {
        Pet pet = findPet(id);
        applyPetRequest(pet, request);
        return PetResponse.from(petRepository.save(pet));
    }

    @CacheEvict(value = {"pets", "petPorId", "planos", "tutores", "tutorPorId"}, allEntries = true)
    @Transactional
    public void excluirPet(Long id) {
        petRepository.delete(findPet(id));
    }

    @Cacheable("planos")
    public Page<PlanoCuidadoResponse> listarPlanos(String busca, StatusCuidado status, Pageable pageable) {
        Page<PlanoCuidado> page;

        if (status != null) {
            page = planoRepository.findByStatus(status, pageable);
        } else if (busca == null || busca.isBlank()) {
            page = planoRepository.findAll(pageable);
        } else {
            String term = busca.trim();
            page = planoRepository.findByTituloContainingIgnoreCaseOrPetNomeContainingIgnoreCase(term, term, pageable);
        }

        return page.map(PlanoCuidadoResponse::from);
    }

    @Cacheable(value = "planoPorId", key = "#id")
    public PlanoCuidadoResponse buscarPlano(Long id) {
        return PlanoCuidadoResponse.from(findPlano(id));
    }

    @CacheEvict(value = {"planos", "planoPorId", "pets", "petPorId"}, allEntries = true)
    @Transactional
    public PlanoCuidadoResponse criarPlano(PlanoCuidadoRequest request) {
        PlanoCuidado plano = new PlanoCuidado();
        applyPlanoRequest(plano, request);
        return PlanoCuidadoResponse.from(planoRepository.save(plano));
    }

    @CacheEvict(value = {"planos", "planoPorId", "pets", "petPorId"}, allEntries = true)
    @Transactional
    public PlanoCuidadoResponse atualizarPlano(Long id, PlanoCuidadoRequest request) {
        PlanoCuidado plano = findPlano(id);
        applyPlanoRequest(plano, request);
        return PlanoCuidadoResponse.from(planoRepository.save(plano));
    }

    @CacheEvict(value = {"planos", "planoPorId", "pets", "petPorId"}, allEntries = true)
    @Transactional
    public void excluirPlano(Long id) {
        planoRepository.delete(findPlano(id));
    }

    private void applyPetRequest(Pet pet, PetRequest request) {
        pet.setNome(request.nome().trim());
        pet.setEspecie(request.especie().trim());
        pet.setRaca(request.raca() == null ? null : request.raca().trim());
        pet.setDataNascimento(request.dataNascimento());
        pet.setPesoKg(request.pesoKg());
        pet.setTutor(findTutor(request.tutorId()));
    }

    private void applyPlanoRequest(PlanoCuidado plano, PlanoCuidadoRequest request) {
        plano.setPet(findPet(request.petId()));
        plano.setTitulo(request.titulo().trim());
        plano.setCategoria(request.categoria());
        plano.setStatus(request.status() == null ? StatusCuidado.PENDENTE : request.status());
        plano.setDataPrevista(request.dataPrevista());
        plano.setObservacao(request.observacao() == null ? null : request.observacao().trim());
    }

    private Tutor findTutor(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tutor nao encontrado."));
    }

    private Pet findPet(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet nao encontrado."));
    }

    private PlanoCuidado findPlano(Long id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano de cuidado nao encontrado."));
    }
}
