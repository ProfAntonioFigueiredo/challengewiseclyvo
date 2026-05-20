package br.fiap.portal.dto;

import br.fiap.portal.model.TAluno;

import java.time.LocalDate;

public record AlunoResponse(
        String rmAluno,
        String nmAluno,
        LocalDate dtNascimento
) {
    public static AlunoResponse from(TAluno aluno) {
        return new AlunoResponse(aluno.getRmAluno(), aluno.getNmAluno(), aluno.getDtNascimento());
    }
}
