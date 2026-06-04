package ifsp.edu.br.IFBANK.Service;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import ifsp.edu.br.IFBANK.Repository.UsuarioRepository;
import ifsp.edu.br.IFBANK.model.Usuario;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void criar(Usuario usuario) {
        repository.save(usuario);
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    }

    public Usuario atualizar(Integer id, Map<String, String> dados) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (dados.containsKey("email")) usuario.setEmail(dados.get("email"));
        if (dados.containsKey("telefone")) usuario.setTelefone(dados.get("telefone"));
        if (dados.containsKey("endereco")) usuario.setEndereco(dados.get("endereco"));

        return repository.save(usuario);
    }
}
