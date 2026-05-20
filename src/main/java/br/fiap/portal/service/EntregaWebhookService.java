package br.fiap.portal.service;

import br.fiap.portal.dto.EntregaDesafioRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class EntregaWebhookService {
    private final HttpClient httpClient;
    private final String webhookUrl;

    public EntregaWebhookService(@Value("${app.webhook.url}") String webhookUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.webhookUrl = webhookUrl;
    }

    public void enviarEntrega(EntregaDesafioRequest request) {
        validar(request);

        if (isBlank(webhookUrl)) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Webhook de entrega nao configurado. Defina APP_WEBHOOK_URL."
            );
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(montarPayload(request)))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "Nao foi possivel enviar a entrega para o webhook."
                );
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Falha ao comunicar com o webhook de entrega."
            );
        } catch (IOException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Falha ao comunicar com o webhook de entrega."
            );
        }
    }

    private void validar(EntregaDesafioRequest request) {
        if (request == null ||
                isBlank(request.getNomeAluno()) ||
                isBlank(request.getRmAluno()) ||
                isBlank(request.getTurma())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe nome, RM e turma antes de enviar a entrega."
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String montarPayload(EntregaDesafioRequest request) {
        String nomeAluno = escapeJson(request.getNomeAluno().trim());
        String rmAluno = escapeJson(request.getRmAluno().trim().toUpperCase());
        String turma = escapeJson(request.getTurma().trim().toUpperCase());

        return """
                {
                  "@type": "MessageCard",
                  "@context": "http://schema.org/extensions",
                  "summary": "Nova entrega CP2",
                  "themeColor": "9D4123",
                  "title": "Entrega de desafio CP2",
                  "sections": [
                    {
                      "activityTitle": "Aluno concluiu o desafio de CRUD com Spring Boot",
                      "facts": [
                        { "name": "Aluno", "value": "%s" },
                        { "name": "RM", "value": "%s" },
                        { "name": "Turma", "value": "%s" }
                      ],
                      "markdown": true
                    }
                  ]
                }
                """.formatted(nomeAluno, rmAluno, turma);
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
