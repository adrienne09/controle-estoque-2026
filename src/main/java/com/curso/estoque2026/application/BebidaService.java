package com.curso.estoque2026.application;
import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.repository.BebidaRepository;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service public class BebidaService { private final BebidaRepository bebidaRepository;
    private final CategoriaBebidaRepository categoriaRepository;
    public BebidaService( BebidaRepository bebidaRepository, CategoriaBebidaRepository categoriaRepository)
    { this.bebidaRepository = bebidaRepository; this.categoriaRepository = categoriaRepository; }
    @Transactional public Bebida cadastrar(Bebida bebida, Long categoriaId)
    { if (bebidaRepository.existsByCodigoBarras( bebida.getCodigoBarras()))
    { throw new RecursoDuplicadoException( "Código de barras já cadastrado"); }
        CategoriaBebida categoria = categoriaRepository.findById(categoriaId) .orElseThrow(() -> new RecursoNaoEncontradoException( "Categoria de bebida não encontrada"));
        categoria.adicionarBebida(bebida); return bebidaRepository.save(bebida); } }

