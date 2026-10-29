package com.curso.estoque2026.application;

import java.util.Set;
import org.springframework.data.domain.Pageable;

// Só aceita ordenar por campos conhecidos, evitando erro 500 com sort arbitrário.
final class Ordenacao {

    private Ordenacao() {
    }

    static void validar(Pageable pageable, Set<String> camposPermitidos) {
        pageable.getSort().forEach(ordem -> {
            if (!camposPermitidos.contains(ordem.getProperty())) {
                throw new IllegalArgumentException(
                    "Campo de ordenação inválido: " + ordem.getProperty());
            }
        });
    }
}
