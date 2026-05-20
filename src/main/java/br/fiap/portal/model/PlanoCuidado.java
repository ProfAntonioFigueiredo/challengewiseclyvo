package br.fiap.portal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "t_plano_cuidado")
public class PlanoCuidado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano_cuidado")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pet", nullable = false)
    private Pet pet;

    @Column(name = "ds_titulo", length = 120, nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_categoria", length = 30, nullable = false)
    private CategoriaCuidado categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_cuidado", length = 30, nullable = false)
    private StatusCuidado status = StatusCuidado.PENDENTE;

    @Column(name = "dt_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(name = "ds_observacao", length = 500)
    private String observacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public CategoriaCuidado getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaCuidado categoria) {
        this.categoria = categoria;
    }

    public StatusCuidado getStatus() {
        return status;
    }

    public void setStatus(StatusCuidado status) {
        this.status = status;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
