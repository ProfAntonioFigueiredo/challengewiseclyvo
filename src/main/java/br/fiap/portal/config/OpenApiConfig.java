package br.fiap.portal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI portalOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PortalWeb Clyvo Vet API")
                        .version("1.0.0")
                        .description("APIs REST para alunos e para continuidade do cuidado veterinario."));
    }
}
