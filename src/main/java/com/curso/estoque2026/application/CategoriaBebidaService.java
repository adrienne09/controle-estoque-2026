package com.curso.estoque2026.application;
import com.curso.estoque2026.domain.CategoriaBebida;
import com.curso.estoque2026.repository.CategoriaBebidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class CategoriaBebidaService { private final CategoriaBebidaRepository repository;
    public CategoriaBebidaService(CategoriaBebidaRepository repository) { this.repository = repository; }
    @Transactional public CategoriaBebida cadastrar(String nome) { if (repository.existsByNomeIgnoreCase(nome))
    { throw new RecursoDuplicadoException( "Nome da categoria já cadastrado"); }
        return repository.save(new CategoriaBebida(nome)); }
    @Transactional(readOnly = true) public CategoriaBebida buscarPorId(Long id)
    { return repository.findById(id) .orElseThrow(() -> new RecursoNaoEncontradoException( "Categoria de bebida não encontrada")); }
    @Transactional(readOnly = true) public List<CategoriaBebida> listar()
    { return repository.findAll(); } }

