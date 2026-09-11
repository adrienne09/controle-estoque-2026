package com.curso.estoque2026.repository;
import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
 import java.util.List; import java.util.Optional;
  public interface BebidaRepository extends JpaRepository<Bebida,
       Long> { Optional<Bebida> findByCodigoBarras(String codigoBarras);
      boolean existsByCodigoBarras(String codigoBarras);
      List<Bebida> findByCategoriaId(Long categoriaId);
      List<Bebida> findByStatus(Status status); }


