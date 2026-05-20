package br.fiap.portal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "t_aluno")
public class TAluno {
    @Id
    @Column(name = "rm_aluno", length = 7, nullable = false)
    private String rmAluno;

    @Column(name = "nm_aluno", length = 80, nullable = false)
    private String nmAluno;

    @Column(name = "dt_nascimento")
    private LocalDate dtNascimento;

    public TAluno() {}
    public TAluno(String rmAluno, String nmAluno, LocalDate dtNascimento) {
        this.rmAluno = rmAluno;
        this.nmAluno = nmAluno;
        this.dtNascimento = dtNascimento;
    }
    public String getRmAluno() { return rmAluno; }
    public void setRmAluno(String rmAluno) { this.rmAluno = rmAluno; }
    public String getNmAluno() { return nmAluno; }
    public void setNmAluno(String nmAluno) { this.nmAluno = nmAluno; }
    public LocalDate getDtNascimento() { return dtNascimento; }
    public void setDtNascimento(LocalDate dtNascimento) { this.dtNascimento = dtNascimento; }
}
