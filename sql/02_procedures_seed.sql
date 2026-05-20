SET SERVEROUTPUT ON

CREATE OR REPLACE PROCEDURE prc_registra_log_erro (
    p_nm_procedure IN VARCHAR2,
    p_cd_erro      IN NUMBER,
    p_ds_erro      IN VARCHAR2
) AS
BEGIN
    INSERT INTO t_log_erro (nm_procedure, nm_usuario, cd_erro, ds_erro)
    VALUES (p_nm_procedure, USER, p_cd_erro, SUBSTR(p_ds_erro, 1, 4000));
END;
/

CREATE OR REPLACE PROCEDURE prc_carga_aluno (
    p_rm_aluno      IN VARCHAR2,
    p_nm_aluno      IN VARCHAR2,
    p_dt_nascimento IN DATE
) AS
BEGIN
    INSERT INTO t_aluno (rm_aluno, nm_aluno, dt_nascimento)
    VALUES (UPPER(TRIM(p_rm_aluno)), TRIM(p_nm_aluno), p_dt_nascimento);
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        prc_registra_log_erro('prc_carga_aluno', SQLCODE, SQLERRM);
    WHEN VALUE_ERROR THEN
        prc_registra_log_erro('prc_carga_aluno', SQLCODE, SQLERRM);
    WHEN OTHERS THEN
        prc_registra_log_erro('prc_carga_aluno', SQLCODE, SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE prc_carga_tutor (
    p_nm_tutor    IN VARCHAR2,
    p_ds_email    IN VARCHAR2,
    p_nr_telefone IN VARCHAR2
) AS
BEGIN
    INSERT INTO t_tutor (nm_tutor, ds_email, nr_telefone)
    VALUES (TRIM(p_nm_tutor), LOWER(TRIM(p_ds_email)), TRIM(p_nr_telefone));
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        prc_registra_log_erro('prc_carga_tutor', SQLCODE, SQLERRM);
    WHEN VALUE_ERROR THEN
        prc_registra_log_erro('prc_carga_tutor', SQLCODE, SQLERRM);
    WHEN OTHERS THEN
        prc_registra_log_erro('prc_carga_tutor', SQLCODE, SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE prc_carga_pet (
    p_nm_pet        IN VARCHAR2,
    p_ds_especie    IN VARCHAR2,
    p_ds_raca       IN VARCHAR2,
    p_dt_nascimento IN DATE,
    p_vl_peso_kg    IN NUMBER,
    p_id_tutor      IN NUMBER
) AS
BEGIN
    INSERT INTO t_pet (nm_pet, ds_especie, ds_raca, dt_nascimento, vl_peso_kg, id_tutor)
    VALUES (TRIM(p_nm_pet), TRIM(p_ds_especie), TRIM(p_ds_raca), p_dt_nascimento, p_vl_peso_kg, p_id_tutor);
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        prc_registra_log_erro('prc_carga_pet', SQLCODE, SQLERRM);
    WHEN VALUE_ERROR THEN
        prc_registra_log_erro('prc_carga_pet', SQLCODE, SQLERRM);
    WHEN OTHERS THEN
        prc_registra_log_erro('prc_carga_pet', SQLCODE, SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE prc_carga_plano_cuidado (
    p_id_pet        IN NUMBER,
    p_ds_titulo     IN VARCHAR2,
    p_tp_categoria  IN VARCHAR2,
    p_st_cuidado    IN VARCHAR2,
    p_dt_prevista   IN DATE,
    p_ds_observacao IN VARCHAR2
) AS
BEGIN
    INSERT INTO t_plano_cuidado (
        id_pet, ds_titulo, tp_categoria, st_cuidado, dt_prevista, ds_observacao
    )
    VALUES (
        p_id_pet,
        TRIM(p_ds_titulo),
        UPPER(TRIM(p_tp_categoria)),
        UPPER(TRIM(p_st_cuidado)),
        p_dt_prevista,
        TRIM(p_ds_observacao)
    );
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        prc_registra_log_erro('prc_carga_plano_cuidado', SQLCODE, SQLERRM);
    WHEN VALUE_ERROR THEN
        prc_registra_log_erro('prc_carga_plano_cuidado', SQLCODE, SQLERRM);
    WHEN OTHERS THEN
        prc_registra_log_erro('prc_carga_plano_cuidado', SQLCODE, SQLERRM);
END;
/

BEGIN
    prc_carga_aluno('RM1001', 'Ana Souza', DATE '2004-02-15');
    prc_carga_aluno('RM1002', 'Bruno Lima', DATE '2003-11-20');

    prc_carga_tutor('Mariana Campos', 'mariana.campos@example.com', '+55 11 99999-0101');
    prc_carga_tutor('Felipe Rocha', 'felipe.rocha@example.com', '+55 11 99999-0202');
    prc_carga_tutor('Carla Mendes', 'carla.mendes@example.com', '+55 11 99999-0303');

    prc_carga_pet('Nina', 'Canino', 'Golden Retriever', DATE '2020-06-10', 26.50, 1);
    prc_carga_pet('Thor', 'Canino', 'SRD', DATE '2018-09-05', 18.20, 2);
    prc_carga_pet('Luna', 'Felino', 'Siamese', DATE '2022-01-18', 4.10, 3);

    prc_carga_plano_cuidado(1, 'Reforco anual de vacina V10', 'VACINA', 'AGENDADO', TRUNC(SYSDATE) + 15, 'Manter carteira de vacinacao atualizada.');
    prc_carga_plano_cuidado(1, 'Check-up preventivo semestral', 'CHECKUP', 'PENDENTE', TRUNC(SYSDATE) + 35, 'Avaliar peso e sinais de envelhecimento.');
    prc_carga_plano_cuidado(2, 'Retorno pos-tratamento dermatologico', 'RETORNO', 'PENDENTE', TRUNC(SYSDATE) + 7, 'Confirmar aderencia ao tratamento.');
    prc_carga_plano_cuidado(2, 'Exame de sangue controle', 'EXAME', 'AGENDADO', TRUNC(SYSDATE) + 20, 'Monitorar marcadores inflamatorios.');
    prc_carga_plano_cuidado(3, 'Alerta preventivo renal', 'ALERTA_RISCO', 'PENDENTE', TRUNC(SYSDATE) + 12, 'Raca com predisposicao, solicitar orientacao.');
    COMMIT;
END;
/
