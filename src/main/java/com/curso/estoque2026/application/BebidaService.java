package com.curso.estoque2026.application;

import com.curso.estoque2026.domain.Bebida;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Fornecedor;
import com.curso.estoque2026.domain.Status;
import com.curso.estoque2026.repository.BebidaRepository;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import com.curso.estoque2026.repository.FornecedorRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        CategoriaBebida categoria = buscarCategoria(categoriaId);
        categoria.adicionarBebida(bebida);
        if (fornecedorId != null) {
            bebida.associarFornecedor(buscarFornecedor(fornecedorId));
        }
        return bebidaRepository.save(bebida);
    }

    @Transactional
    public Bebida cadastrar(Bebida bebida, Long categoriaId) {
        return cadastrar(bebida, categoriaId, null);
    }

    @Transactional(readOnly = true)
    public Bebida buscarPorId(Long id) {
        return buscarEntidade(id);
    }

    @Transactional(readOnly = true)
    public List<Bebida> listar() {
        return bebidaRepository.findAll();
    }

    @Transactional
    public Bebida alterar(
        Long id,
        String descricao,
        BigDecimal valorUnitario,
        BigDecimal estoqueMinimo,
        Long categoriaId,
        Long fornecedorId) {
        Bebida bebida = buscarEntidade(id);
        CategoriaBebida categoria = buscarCategoria(categoriaId);
        Fornecedor fornecedor = fornecedorId == null ? null : buscarFornecedor(fornecedorId);
        bebida.alterarDescricao(descricao);
        bebida.alterarValorUnitario(valorUnitario);
        bebida.alterarEstoqueMinimo(estoqueMinimo);
        bebida.alterarCategoria(categoria);
        bebida.alterarFornecedor(fornecedor);
        return bebida;
    }

    @Transactional
    public Bebida alterarStatus(Long id, Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        Bebida bebida = buscarEntidade(id);
        if (status == Status.ATIVO) {
            bebida.ativar();
        } else {
            bebida.inativar();
        }
        return bebida;
    }

    @Transactional
    public Bebida receberEstoque(Long id, BigDecimal quantidade) {
        Bebida bebida = buscarEntidade(id);
        bebida.receberEstoque(quantidade);
        return bebida;
    }

    @Transactional
    public Bebida retirarEstoque(Long id, BigDecimal quantidade) {
        Bebida bebida = buscarEntidade(id);
        bebida.retirarEstoque(quantidade);
        return bebida;
    }

    @Transactional
    public void excluir(Long id) {
        Bebida bebida = buscarEntidade(id);
        bebidaRepository.delete(bebida);
        bebidaRepository.flush();
    }

    @Transactional(readOnly = true)
    public Page<Bebida> pesquisar(
        String descricao,
        Status status,
        Long categoriaId,
        Long fornecedorId,
        Boolean abaixoEstoqueMinimo,
        Pageable pageable) {
        Ordenacao.validar(pageable, Set.of("id", "codigoBarras", "descricao", "saldoEstoque",
            "valorUnitario", "estoqueMinimo", "dataCadastro", "status"));
        Specification<Bebida> filtros = (root, query, cb) -> cb.conjunction();
        if (descricao != null && !descricao.isBlank()) {
            String trecho = "%" + descricao.trim().toLowerCase(Locale.ROOT) + "%";
            filtros = filtros.and((root, query, cb) -> cb.like(cb.lower(root.get("descricao")), trecho));
        }
        if (status != null) {
            filtros = filtros.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (categoriaId != null) {
            filtros = filtros.and((root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId));
        }
        if (fornecedorId != null) {
            filtros = filtros.and((root, query, cb) -> cb.equal(root.get("fornecedor").get("id"), fornecedorId));
        }
        if (Boolean.TRUE.equals(abaixoEstoqueMinimo)) {
            filtros = filtros.and((root, query, cb) ->
                cb.lessThan(root.get("saldoEstoque"), root.get("estoqueMinimo")));
        }
        return bebidaRepository.findAll(filtros, pageable);
    }

    private Bebida buscarEntidade(Long id) {
        return bebidaRepository.buscarPorIdComRelacionamentos(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Bebida nao encontrada"));
    }

    private CategoriaBebida buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria de bebida nao encontrada"));
    }

    private Fornecedor buscarFornecedor(Long id) {
        return fornecedorRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor nao encontrado"));
    }
}
