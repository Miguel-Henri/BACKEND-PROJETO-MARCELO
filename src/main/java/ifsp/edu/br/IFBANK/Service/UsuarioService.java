package ifsp.edu.br.IFBANK.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

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

        if (dados.containsKey("email"))    usuario.setEmail(dados.get("email"));
        if (dados.containsKey("telefone")) usuario.setTelefone(dados.get("telefone"));
        if (dados.containsKey("endereco")) usuario.setEndereco(dados.get("endereco"));
        if (dados.containsKey("nome"))     usuario.setNome(dados.get("nome"));

        return repository.save(usuario);
    }

    /**
     * Login simples por e-mail e senha (sem Spring Security).
     *
     * Retorna um mapa com os dados do usuário e das suas contas,
     * incluindo o tipo (CLIENTE ou GERENTE) para que o frontend
     * saiba para qual tela redirecionar.
     *
     * ATENÇÃO: em produção, a senha deve ser armazenada com hash (BCrypt).
     * Aqui a comparação é direta por ser um projeto acadêmico.
     */
    public Map<String, Object> login(String email, String senha) {
        Usuario usuario = repository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos"));

        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        // Busca as contas associadas ao usuário
        List<Conta> contas = contaRepository.findAll()
            .stream()
            .filter(c -> c.getUsuario() != null && c.getUsuario().getId().equals(usuario.getId()))
            .toList();

        // Verifica se alguma conta está ativa (necessário para liberar o login)
        boolean temContaAtiva = contas.stream()
            .anyMatch(c -> c.getStatus() == StatusConta.ATIVA);

        if (!temContaAtiva && !contas.isEmpty()) {
            throw new IllegalStateException("Sua conta ainda não foi aprovada pelo gerente");
        }

        // Monta a resposta com os dados relevantes para o frontend
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("usuarioId", usuario.getId());
        resposta.put("nome", usuario.getNome());
        resposta.put("email", usuario.getEmail());
        resposta.put("telefone", usuario.getTelefone());
        resposta.put("endereco", usuario.getEndereco());
        resposta.put("fotoPerfil", usuario.getFotoPerfil());
        resposta.put("status", usuario.getStatus());

        // Inclui lista resumida das contas (id, numeroConta, tipo, status)
        List<Map<String, Object>> contasResumo = contas.stream().map(c -> {
            Map<String, Object> mc = new HashMap<>();
            mc.put("contaId", c.getId());
            mc.put("numeroConta", c.getNumeroConta());
            mc.put("agencia", c.getAgencia());
            mc.put("tipo", c.getTipo());
            mc.put("status", c.getStatus());
            mc.put("saldo", c.getSaldo());
            return mc;
        }).toList();

        resposta.put("contas", contasResumo);

        return resposta;
    }
}
