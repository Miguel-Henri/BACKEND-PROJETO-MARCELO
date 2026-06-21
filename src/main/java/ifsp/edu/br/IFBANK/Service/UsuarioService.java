package ifsp.edu.br.IFBANK.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ifsp.edu.br.IFBANK.Repository.ContaRepository;
import ifsp.edu.br.IFBANK.Repository.UsuarioRepository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.Usuario;

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

    @Transactional
    public Map<String, Object> dadosSessao(Usuario usuario) {
        List<Map<String, Object>> contas = new ArrayList<>();
        if (usuario.getContas() != null) {
            for (Conta conta : usuario.getContas()) {
                Map<String, Object> contaMap = new HashMap<>();
                contaMap.put("contaId", conta.getId());
                contaMap.put("numeroConta", conta.getNumeroConta());
                contaMap.put("agencia", conta.getAgencia());
                contaMap.put("tipo", conta.getRole());
                contaMap.put("status", conta.getStatus());
                contaMap.put("saldo", conta.getSaldo());
                contas.add(contaMap);
            }
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("usuarioId", usuario.getId());
        resposta.put("nome", usuario.getNome());
        resposta.put("email", usuario.getEmail());
        resposta.put("telefone", usuario.getTelefone());
        resposta.put("endereco", usuario.getEndereco());
        resposta.put("fotoPerfil", usuario.getFotoPerfil());
        resposta.put("contas", contas);
        return resposta;
    }
}