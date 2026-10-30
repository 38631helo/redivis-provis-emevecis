package escola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import escola.model.Sala;
import escola.repository.EscolaRepository;

@Service
public class EscolaService {

    private final EscolaRepository repository;

    public EscolaService(EscolaRepository repository) {
        this.repository = repository;
    }

    // Listar todas as salas
    public List<Sala> listar() {
        return repository.findAll();
    }

    // Buscar sala pelo ID
    public Optional<Sala> buscar(Long id) {
        return repository.findById(id);
    }

    // Cadastrar sala
    public Sala criar(Sala sala) {
        return repository.save(sala);
    }

    // Excluir sala
    public boolean excluir(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}