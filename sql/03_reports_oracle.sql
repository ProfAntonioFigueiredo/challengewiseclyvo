SET SERVEROUTPUT ON

PROMPT Bloco anonimo 1: tres consultas com joins, group by e order by
DECLARE
BEGIN
    DBMS_OUTPUT.PUT_LINE('1) Planos por tutor e status');
    FOR r IN (
        SELECT tu.nm_tutor, pc.st_cuidado, COUNT(*) total_planos
        FROM t_tutor tu
        JOIN t_pet pe ON pe.id_tutor = tu.id_tutor
        JOIN t_plano_cuidado pc ON pc.id_pet = pe.id_pet
        GROUP BY tu.nm_tutor, pc.st_cuidado
        ORDER BY tu.nm_tutor, pc.st_cuidado
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(r.nm_tutor || ' | ' || r.st_cuidado || ' | ' || r.total_planos);
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('2) Peso medio por especie');
    FOR r IN (
        SELECT pe.ds_especie, COUNT(*) total_pets, ROUND(AVG(pe.vl_peso_kg), 2) peso_medio
        FROM t_tutor tu
        JOIN t_pet pe ON pe.id_tutor = tu.id_tutor
        GROUP BY pe.ds_especie
        ORDER BY peso_medio DESC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(r.ds_especie || ' | ' || r.total_pets || ' | ' || r.peso_medio);
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('3) Proximos cuidados por pet');
    FOR r IN (
        SELECT pe.nm_pet, tu.nm_tutor, COUNT(pc.id_plano_cuidado) total_cuidados
        FROM t_tutor tu
        JOIN t_pet pe ON pe.id_tutor = tu.id_tutor
        JOIN t_plano_cuidado pc ON pc.id_pet = pe.id_pet
        GROUP BY pe.nm_pet, tu.nm_tutor
        ORDER BY total_cuidados DESC, pe.nm_pet
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(r.nm_pet || ' | ' || r.nm_tutor || ' | ' || r.total_cuidados);
    END LOOP;
END;
/

PROMPT Bloco anonimo 2: leitura linha atual, anterior e proxima
DECLARE
BEGIN
    FOR r IN (
        SELECT
            ds_titulo,
            dt_prevista atual,
            LAG(dt_prevista) OVER (ORDER BY dt_prevista, id_plano_cuidado) anterior,
            LEAD(dt_prevista) OVER (ORDER BY dt_prevista, id_plano_cuidado) proxima
        FROM t_plano_cuidado
        ORDER BY dt_prevista, id_plano_cuidado
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(
            r.ds_titulo ||
            ' | atual=' || TO_CHAR(r.atual, 'YYYY-MM-DD') ||
            ' | anterior=' || NVL(TO_CHAR(r.anterior, 'YYYY-MM-DD'), 'Vazio') ||
            ' | proxima=' || NVL(TO_CHAR(r.proxima, 'YYYY-MM-DD'), 'Vazio')
        );
    END LOOP;
END;
/

PROMPT Relatorio 1: cursor explicito com tomada de decisao
DECLARE
    CURSOR c_planos IS
        SELECT pc.ds_titulo, pc.st_cuidado, pc.dt_prevista, pe.nm_pet
        FROM t_plano_cuidado pc
        JOIN t_pet pe ON pe.id_pet = pc.id_pet
        ORDER BY pc.dt_prevista;
BEGIN
    FOR r IN c_planos LOOP
        IF r.dt_prevista <= TRUNC(SYSDATE) + 10 THEN
            DBMS_OUTPUT.PUT_LINE('Prioridade alta: ' || r.nm_pet || ' - ' || r.ds_titulo);
        ELSE
            DBMS_OUTPUT.PUT_LINE('Prioridade normal: ' || r.nm_pet || ' - ' || r.ds_titulo);
        END IF;
    END LOOP;
END;
/

PROMPT Relatorio 2: cursor explicito com sumarizacao numerica
DECLARE
    CURSOR c_pets IS
        SELECT ds_especie, nm_pet, vl_peso_kg
        FROM t_pet
        ORDER BY ds_especie, nm_pet;
    v_total_peso NUMBER := 0;
    v_total_pets NUMBER := 0;
BEGIN
    FOR r IN c_pets LOOP
        v_total_pets := v_total_pets + 1;
        v_total_peso := v_total_peso + NVL(r.vl_peso_kg, 0);
        IF r.vl_peso_kg >= 20 THEN
            DBMS_OUTPUT.PUT_LINE(r.nm_pet || ' possui porte maior.');
        ELSE
            DBMS_OUTPUT.PUT_LINE(r.nm_pet || ' possui porte menor/medio.');
        END IF;
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('Total de pets=' || v_total_pets || ' | Peso somado=' || v_total_peso);
END;
/

PROMPT Relatorio 3: cursor explicito com agrupamento por categoria
DECLARE
    CURSOR c_categoria IS
        SELECT tp_categoria, COUNT(*) total_planos
        FROM t_plano_cuidado
        GROUP BY tp_categoria
        ORDER BY total_planos DESC;
BEGIN
    FOR r IN c_categoria LOOP
        CASE
            WHEN r.total_planos >= 2 THEN
                DBMS_OUTPUT.PUT_LINE(r.tp_categoria || ': categoria recorrente');
            ELSE
                DBMS_OUTPUT.PUT_LINE(r.tp_categoria || ': categoria pontual');
        END CASE;
    END LOOP;
END;
/

PROMPT Relatorio 4: cursor explicito listando dados completos
DECLARE
    CURSOR c_jornada IS
        SELECT tu.nm_tutor, pe.nm_pet, pc.ds_titulo, pc.st_cuidado
        FROM t_tutor tu
        JOIN t_pet pe ON pe.id_tutor = tu.id_tutor
        JOIN t_plano_cuidado pc ON pc.id_pet = pe.id_pet
        ORDER BY tu.nm_tutor, pe.nm_pet, pc.dt_prevista;
BEGIN
    FOR r IN c_jornada LOOP
        IF r.st_cuidado IN ('PENDENTE', 'ATRASADO') THEN
            DBMS_OUTPUT.PUT_LINE('Acao requerida: ' || r.nm_tutor || ' | ' || r.nm_pet || ' | ' || r.ds_titulo);
        ELSE
            DBMS_OUTPUT.PUT_LINE('Acompanhamento: ' || r.nm_tutor || ' | ' || r.nm_pet || ' | ' || r.ds_titulo);
        END IF;
    END LOOP;
END;
/
