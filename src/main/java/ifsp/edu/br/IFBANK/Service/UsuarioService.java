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

    /**
     * Login simples por e-mail e senha.
     *
     * Retorna os dados do usuário e das suas contas,
     * incluindo o tipo CLIENTE ou GERENTE para o frontend redirecionar.
     */
    public Map<String, Object> login(String email, String senha) {
        if (email == null || senha == null) {
            throw new CredenciaisInvalidasException("E-mail e senha são obrigatórios.");
        }
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos."));

        if (!usuario.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        List<Conta> contas = contaRepository.findAll()
                .stream()
                .filter(c -> c.getUsuario() != null && c.getUsuario().getId().equals(usuario.getId()))
                .toList();

        boolean temContaAtiva = contas.stream()
                .anyMatch(c -> c.getStatus() == StatusConta.ATIVA);

        if (!temContaAtiva && !contas.isEmpty()) {
            throw new IllegalStateException("Sua conta ainda não foi aprovada pelo gerente");
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("usuarioId", usuario.getId());
        resposta.put("nome", usuario.getNome());
        resposta.put("email", usuario.getEmail());
        resposta.put("telefone", usuario.getTelefone());
        resposta.put("endereco", usuario.getEndereco());
        resposta.put("fotoPerfil", usuario.getFotoPerfil());
        resposta.put("status", usuario.getStatus());

        List<Map<String, Object>> contasResumo = contas.stream().map(c -> {
            Map<String, Object> contaResumo = new HashMap<>();
            contaResumo.put("contaId", c.getId());
            contaResumo.put("numeroConta", c.getNumeroConta());
            contaResumo.put("agencia", c.getAgencia());
            contaResumo.put("tipo", c.getTipo());
            contaResumo.put("status", c.getStatus());
            contaResumo.put("saldo", c.getSaldo());
            return contaResumo;
        }).toList();

        resposta.put("contas", contasResumo);

        return resposta;
    }
}