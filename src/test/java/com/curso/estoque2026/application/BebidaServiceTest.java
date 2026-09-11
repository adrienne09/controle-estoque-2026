package com.curso.estoque2026.application;
import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.repository.BebidaRepository;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest @ActiveProfiles("test") @Transactional class BebidaServiceTest { @Autowired private BebidaService bebidaService;
    @Autowired private CategoriaBebidaRepository categoriaRepository;
    @Autowired private BebidaRepository bebidaRepository;
    @Test void deveCadastrarBebidaComCategoria() { CategoriaBebida categoria = categoriaRepository.save( new CategoriaBebida("Refrigerantes"));
        Bebida cadastrada = bebidaService.cadastrar( novaBebida("7890000000001"), categoria.getId());
        assertNotNull(cadastrada.getId()); assertEquals(categoria.getId(), cadastrada.getCategoria().getId()); }
    @Test void naoDeveCadastrarBebidaComCategoriaInexistente() { Bebida bebida = novaBebida("7890000000002");
        assertThrows( RecursoNaoEncontradoException.class, () -> bebidaService.cadastrar(bebida, Long.MAX_VALUE));
        assertFalse(bebidaRepository.existsByCodigoBarras( bebida.getCodigoBarras())); }
    private Bebida novaBebida(String codigoBarras) { return new Bebida( codigoBarras, "Bebida de teste", BigDecimal.TEN, BigDecimal.valueOf(5.50), LocalDate.now()); } }

