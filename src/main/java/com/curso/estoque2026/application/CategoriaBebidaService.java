package com.curso.estoque2026.application;

import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.domain.Status;
import com.curso.estoque2026.repository.BebidaRepository;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaBebidaService {

    private final CategoriaBebidaRepository repository;
    private final BebidaRepository bebidaRepository;

    public CategoriaBebidaService(
        CategoriaBebidaRepository repository,
        BebidaRepository bebidaRepository) {
        this.repository = repository;
        this.bebidaRepository = bebidaRepository;
    }

    @Transactional
    public CategoriaBebida cadastrar(String nome) {
        String nomeNormalizado = normalizarNome(nome);
        if (repository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RecursoDuplicadoException("Nome da categoria já cadastrado");
        }
        return repository.save(new CategoriaBebida(nomeNormalizado));
    }

    @Transactional
    public CategoriaBebida alterar(Long id, String nome) {
        String nomeNormalizado = normalizarNome(nome);
        CategoriaBebida categoria = buscarEntidade(id);
        if (repository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RecursoDuplicadoException("Nome da categoria já cadastrado");
        }
        categoria.alterarNome(nomeNormalizado);
        return categoria;
    }

    @Transactional
    public CategoriaBebida alterarStatus(Long id, Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        CategoriaBebida categoria = buscarEntidade(id);
        if (status == Status.ATIVO) {
            categoria.ativar();
        } else {
            categoria.inativar();
        }
        return categoria;
    }

    @Transactional
    public void excluir(Long id) {
        CategoriaBebida categoria = buscarEntidade(id);
        if (bebidaRepository.existsByCategoriaId(id)) {
            throw new RecursoEmUsoException(
                "Categoria de bebida não pode ser excluída porque possui bebidas");
        }
        repository.delete(categoria);
        repository.flush();
    }

    @Transactional(readOnly = true)
    public CategoriaBebida buscarPorId(Long id) {
        return buscarEntidade(id);
    }

    @Transactional(readOnly = true)
    public List<CategoriaBebida> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CategoriaBebida> pesquisar(String nome, Status status, Pageable pageable) {
        Ordenacao.validar(pageable, Set.of("id", "nome", "status"));
        Specification<CategoriaBebida> filtros = (root, query, cb) -> cb.conjunction();
        if (nome != null && !nome.isBlank()) {
            String trecho = "%" + nome.trim().toLowerCase(Locale.ROOT) + "%";
            filtros = filtros.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")), trecho));
        }
        if (status != null) {
            filtros = filtros.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        return repository.findAll(filtros, pageable);
    }

    private CategoriaBebida buscarEntidade(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException(
                "Categoria de bebida não encontrada"));
    }

    private String normalizarNome(String nome) {
        return nome == null ? null : nome.trim();
    }
}
