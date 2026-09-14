package com.curso.estoque2026.repository;
import com.curso.estoque2026.domain.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FornecedorRepository
    extends JpaRepository<Fornecedor, Long> { boolean existsByCnpj(String cnpj);}
