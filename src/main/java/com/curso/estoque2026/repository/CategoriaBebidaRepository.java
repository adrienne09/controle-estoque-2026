package com.curso.estoque2026.repository;
import com.curso.estoque2026.domain.CategoriaBebida;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CategoriaBebidaRepository
    extends JpaRepository<CategoriaBebida,
    Long> { boolean existsByNomeIgnoreCase(String nome);
    Optional<CategoriaBebida> findByNomeIgnoreCase(String nome); }

