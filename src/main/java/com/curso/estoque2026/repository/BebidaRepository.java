package com.curso.estoque2026.repository;

import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BebidaRepository extends JpaRepository<Bebida, Long>, JpaSpecificationExecutor<Bebida> {

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

    boolean existsByCategoriaId(Long categoriaId);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    List<Bebida> findByStatus(Status status);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    @Query("select b from Bebida b where b.id = :id")
    Optional<Bebida> buscarPorIdComRelacionamentos(@Param("id") Long id);

    // Carrega categoria e fornecedor junto da página, evitando LazyInitializationException no mapper.
    @Override
    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    Page<Bebida> findAll(Specification<Bebida> specification, Pageable pageable);
}
