package com.curso.estoque2026.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Verificação de disponibilidade da API")
@RequestMapping("/api/health")
public class HealthController {

    @Operation(summary = "Verificar se a API está no ar")
    @GetMapping
    public String health() {
        return "OK";
    }
}
