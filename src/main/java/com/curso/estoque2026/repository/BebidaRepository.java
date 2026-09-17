package com.curso.estoque2026.repository;

import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BebidaRepository extends JpaRepository<Bebida, Long> {

    @Override
    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    List<Bebida> findAll();

    @Override
    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    Optional<Bebida> findById(Long id);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    Optional<Bebida> findByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarras(String codigoBarras);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    List<Bebida> findByCategoriaId(Long categoriaId);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    List<Bebida> findByStatus(Status status);
}


