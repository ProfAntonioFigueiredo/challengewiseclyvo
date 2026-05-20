package br.fiap.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EntregaDesafioRequest {
    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 120, message = "Nome deve ter no maximo 120 caracteres")
    private String nomeAluno;

    @NotBlank(message = "RM e obrigatorio")
    @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "RM deve ter de 2 a 10 letras ou numeros")
    private String rmAluno;

    @NotBlank(message = "Turma e obrigatoria")
    @Size(max = 30, message = "Turma deve ter no maximo 30 caracteres")
    private String turma;

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getRmAluno() {
        return rmAluno;
    }

    public void setRmAluno(String rmAluno) {
        this.rmAluno = rmAluno;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }
}
