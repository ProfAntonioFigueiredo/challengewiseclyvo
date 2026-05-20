const form = document.getElementById("aluno-form");
const rmInput = document.getElementById("rmAluno");
const nomeInput = document.getElementById("nmAluno");
const dataInput = document.getElementById("dtNascimento");
const submitButton = document.getElementById("submit-button");
const clearButton = document.getElementById("clear-button");
const reloadButton = document.getElementById("reload-button");
const searchInput = document.getElementById("search-input");
const tbody = document.getElementById("alunos-tbody");
const statusMessage = document.getElementById("status-message");
const formMode = document.getElementById("form-mode");
const entregaForm = document.getElementById("entrega-form");
const entregaNomeInput = document.getElementById("entregaNome");
const entregaRmInput = document.getElementById("entregaRm");
const entregaTurmaInput = document.getElementById("entregaTurma");
const entregaButton = document.getElementById("entrega-button");
const entregaStatus = document.getElementById("entrega-status");

let alunos = [];
let editingRm = null;

function setStatus(message, type = "") {
    statusMessage.textContent = message;
    statusMessage.className = "status";

    if (type) {
        statusMessage.classList.add(`status--${type}`);
    }
}

function resetForm() {
    editingRm = null;
    form.reset();
    rmInput.disabled = false;
    submitButton.textContent = "Salvar aluno";
    formMode.textContent = "Modo cadastro";
}

function renderTable(items) {
    if (!items.length) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" class="empty-state">Nenhum aluno encontrado.</td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = items.map((aluno) => `
        <tr>
            <td>${aluno.rmAluno}</td>
            <td>${aluno.nmAluno}</td>
            <td>${formatDate(aluno.dtNascimento)}</td>
            <td>
                <div class="table__actions">
                    <button type="button" class="action-link" data-action="edit" data-rm="${aluno.rmAluno}">Editar</button>
                    <button type="button" class="action-link action-link--danger" data-action="delete" data-rm="${aluno.rmAluno}">Excluir</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function formatDate(value) {
    if (!value) {
        return "-";
    }

    const [year, month, day] = value.split("-");
    return `${day}/${month}/${year}`;
}

function getFilteredAlunos() {
    const term = searchInput.value.trim().toLowerCase();

    if (!term) {
        return alunos;
    }

    return alunos.filter((aluno) =>
        aluno.rmAluno.toLowerCase().includes(term) ||
        aluno.nmAluno.toLowerCase().includes(term)
    );
}

async function loadAlunos() {
    setStatus("Carregando alunos...");

    try {
        const response = await fetch("/alunos?size=100&sort=nmAluno,asc");

        if (!response.ok) {
            throw new Error("Nao foi possivel carregar os alunos.");
        }

        const payload = await response.json();
        alunos = Array.isArray(payload) ? payload : (payload.content ?? []);
        renderTable(getFilteredAlunos());
        setStatus(`${alunos.length} aluno(s) carregado(s).`, "success");
    } catch (error) {
        renderTable([]);
        setStatus(error.message, "error");
    }
}

function setEntregaStatus(message, type = "") {
    entregaStatus.textContent = message;
    entregaStatus.className = "form__mode";

    if (type) {
        entregaStatus.classList.add(`status--${type}`);
    }
}

function salvarDadosEntregaLocalmente() {
    const payload = {
        nomeAluno: entregaNomeInput.value,
        rmAluno: entregaRmInput.value,
        turma: entregaTurmaInput.value
    };

    window.localStorage.setItem("cp2-entrega", JSON.stringify(payload));
}

function carregarDadosEntregaLocalmente() {
    const rawValue = window.localStorage.getItem("cp2-entrega");

    if (!rawValue) {
        return;
    }

    try {
        const payload = JSON.parse(rawValue);
        entregaNomeInput.value = payload.nomeAluno ?? "";
        entregaRmInput.value = payload.rmAluno ?? "";
        entregaTurmaInput.value = payload.turma ?? "";
    } catch (error) {
        window.localStorage.removeItem("cp2-entrega");
    }
}

function fillForm(aluno) {
    editingRm = aluno.rmAluno;
    rmInput.value = aluno.rmAluno;
    nomeInput.value = aluno.nmAluno;
    dataInput.value = aluno.dtNascimento;
    rmInput.disabled = true;
    submitButton.textContent = "Atualizar aluno";
    formMode.textContent = `Modo edicao do aluno ${aluno.rmAluno}`;
    setStatus(`Editando o aluno ${aluno.rmAluno}.`, "success");
}

async function saveAluno(event) {
    event.preventDefault();

    const payload = {
        rmAluno: rmInput.value.trim().toUpperCase(),
        nmAluno: nomeInput.value.trim(),
        dtNascimento: dataInput.value
    };

    if (!payload.rmAluno || !payload.nmAluno || !payload.dtNascimento) {
        setStatus("Preencha todos os campos antes de salvar.", "error");
        return;
    }

    const isEditing = Boolean(editingRm);
    const url = isEditing ? `/alunos/${editingRm}` : "/alunos";
    const method = isEditing ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const error = await extractError(response);
            throw new Error(error || (isEditing ? "Nao foi possivel atualizar o aluno." : "Nao foi possivel cadastrar o aluno."));
        }

        resetForm();
        await loadAlunos();
        setStatus(isEditing ? "Aluno atualizado com sucesso." : "Aluno cadastrado com sucesso.", "success");
    } catch (error) {
        setStatus(error.message, "error");
    }
}

async function deleteAluno(rmAluno) {
    const confirmed = window.confirm(`Deseja remover o aluno ${rmAluno}?`);

    if (!confirmed) {
        return;
    }

    try {
        const response = await fetch(`/alunos/${rmAluno}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            const error = await extractError(response);
            throw new Error(error || "Nao foi possivel excluir o aluno.");
        }

        if (editingRm === rmAluno) {
            resetForm();
        }

        await loadAlunos();
        setStatus("Aluno removido com sucesso.", "success");
    } catch (error) {
        setStatus(error.message, "error");
    }
}

async function enviarEntrega(event) {
    event.preventDefault();

    const payload = {
        nomeAluno: entregaNomeInput.value.trim(),
        rmAluno: entregaRmInput.value.trim().toUpperCase(),
        turma: entregaTurmaInput.value.trim().toUpperCase()
    };

    if (!payload.nomeAluno || !payload.rmAluno || !payload.turma) {
        setEntregaStatus("Informe nome, RM e turma antes de enviar.", "error");
        return;
    }

    salvarDadosEntregaLocalmente();
    entregaButton.disabled = true;
    setEntregaStatus("Enviando entrega para o webhook...");

    try {
        const response = await fetch("/entregas", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const error = await extractError(response);
            throw new Error(error || "Nao foi possivel enviar a entrega.");
        }

        setEntregaStatus("Entrega enviada com sucesso para o professor.", "success");
    } catch (error) {
        setEntregaStatus(error.message, "error");
    } finally {
        entregaButton.disabled = false;
    }
}

async function extractError(response) {
    try {
        const body = await response.json();
        const fieldMessages = body.fields ? Object.values(body.fields).join(" ") : "";
        return [body.message, fieldMessages].filter(Boolean).join(" ");
    } catch (error) {
        return "";
    }
}

tbody.addEventListener("click", (event) => {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const { action, rm } = button.dataset;
    const aluno = alunos.find((item) => item.rmAluno === rm);

    if (!aluno) {
        return;
    }

    if (action === "edit") {
        fillForm(aluno);
    }

    if (action === "delete") {
        deleteAluno(rm);
    }
});

searchInput.addEventListener("input", () => {
    renderTable(getFilteredAlunos());
});

form.addEventListener("submit", saveAluno);
entregaForm.addEventListener("submit", enviarEntrega);
clearButton.addEventListener("click", resetForm);
reloadButton.addEventListener("click", loadAlunos);
entregaNomeInput.addEventListener("input", salvarDadosEntregaLocalmente);
entregaRmInput.addEventListener("input", salvarDadosEntregaLocalmente);
entregaTurmaInput.addEventListener("input", salvarDadosEntregaLocalmente);

carregarDadosEntregaLocalmente();
loadAlunos();
