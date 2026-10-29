package com.curso.estoque2026.repository;

import com.curso.estoque2026.domain.CategoriaBebida;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoriaBebidaRepository extends
    JpaRepository<CategoriaBebida, Long>,
    JpaSpecificationExecutor<CategoriaBebida> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    Optional<CategoriaBebida> findByNomeIgnoreCase(String nome);
}
