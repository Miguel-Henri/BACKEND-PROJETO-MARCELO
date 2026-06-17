package ifsp.edu.br.IFBANK.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import ifsp.edu.br.IFBANK.Exception.CredenciaisInvalidasException;

import ifsp.edu.br.IFBANK.Repository.ContaRepository;
import ifsp.edu.br.IFBANK.Repository.UsuarioRepository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.Usuario;
import ifsp.edu.br.IFBANK.model.enums.StatusConta;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ContaRepository contaRepository;

    public UsuarioService(UsuarioRepository repository, ContaRepository contaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
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

        if (dados.containsKey("email")) {
            usuario.setEmail(dados.get("email"));
        }

        if (dados.containsKey("telefone")) {
            usuario.setTelefone(dados.get("telefone"));
        }

        if (dados.containsKey("endereco")) {
            usuario.setEndereco(dados.get("endereco"));
        }

        if (dados.containsKey("nome")) {
            usuario.setNome(dados.get("nome"));
        }

        return repository.save(usuario);
    }


}