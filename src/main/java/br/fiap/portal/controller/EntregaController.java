package br.fiap.portal.controller;

import br.fiap.portal.dto.EntregaDesafioRequest;
import br.fiap.portal.service.EntregaWebhookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/entregas")
public class EntregaController {
    private final EntregaWebhookService entregaWebhookService;

    public EntregaController(EntregaWebhookService entregaWebhookService) {
        this.entregaWebhookService = entregaWebhookService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> enviarEntrega(@Valid @RequestBody EntregaDesafioRequest request) {
        entregaWebhookService.enviarEntrega(request);
        return ResponseEntity.ok(Map.of("message", "Entrega enviada com sucesso."));
    }
}
