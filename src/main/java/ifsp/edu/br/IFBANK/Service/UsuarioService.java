package ifsp.edu.br.IFBANK.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import ifsp.edu.br.IFBANK.Repository.UsuarioRepository;
import ifsp.edu.br.IFBANK.model.Usuario;
@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void criar(Usuario usuario){
        repository.save(usuario);
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
    
    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }
}
