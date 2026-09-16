package com.curso.estoque2026.application;

import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Fornecedor;
import com.curso.estoque2026.repository.BebidaRepository;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import com.curso.estoque2026.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BebidaService {

    private final BebidaRepository bebidaRepository;
    private final CategoriaBebidaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    public BebidaService(
        BebidaRepository bebidaRepository,
        CategoriaBebidaRepository categoriaRepository,
        FornecedorRepository fornecedorRepository) {
        this.bebidaRepository = bebidaRepository;
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public Bebida cadastrar(Bebida bebida, Long categoriaId, Long fornecedorId) {
        if (bebidaRepository.existsByCodigoBarras(bebida.getCodigoBarras())) {
            throw new RecursoDuplicadoException("Codigo de barras ja cadastrado");
        }

        CategoriaBebida categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException(
                "Categoria de bebida nao encontrada"));

        categoria.adicionarBebida(bebida);

        if (fornecedorId != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                    "Fornecedor nao encontrado"));
            bebida.associarFornecedor(fornecedor);
        }

        return bebidaRepository.save(bebida);
    }

    @Transactional
    public Bebida cadastrar(Bebida bebida, Long categoriaId) {
        return cadastrar(bebida, categoriaId, null);
    }

    @Transactional(readOnly = true)
    public Bebida buscarPorId(Long id) {
        return bebidaRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException(
                "Bebida nao encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Bebida> listar() {
        return bebidaRepository.findAll();
    }
}
