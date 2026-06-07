package ifsp.edu.br.IFBANK.Controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.UsuarioService;
import ifsp.edu.br.IFBANK.model.Usuario;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Cadastra um novo usuário (abertura de conta).
     * POST /api/usuarios/novo
     */
    @PostMapping("/novo")
    public ResponseEntity<String> criarUser(@RequestBody Usuario usuario) {
        try {
            usuarioService.criar(usuario);
            return ResponseEntity.status(201).body("Cadastro realizado com sucesso. Aguarde a aprovação do gerente.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("E-mail já cadastrado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar usuário.");
        }
    }

    /**
     * Busca um usuário pelo ID.
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Atualiza dados pessoais do usuário (nome, e-mail, telefone, endereço).
     * PATCH /api/usuarios/{id}
     *
     * Body JSON (apenas os campos que deseja alterar):
     * { "telefone": "11999999999", "endereco": "Rua X, 100" }
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody Map<String, String> dados) {
        try {
            return ResponseEntity.ok(usuarioService.atualizar(id, dados));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("E-mail já em uso.");
        }
    }

    /**
     * Realiza o login do usuário.
     * POST /api/usuarios/login
     *
     * Body JSON:
     * { "email": "usuario@email.com", "senha": "minhasenha" }
     *
     * Retorna os dados do usuário e suas contas (incluindo tipo: CLIENTE ou GERENTE).
     * O frontend usa o campo "tipo" para redirecionar para a tela correta.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        try {
            String email = credenciais.get("email");
            String senha = credenciais.get("senha");

            if (email == null || senha == null) {
                return ResponseEntity.badRequest().body("E-mail e senha são obrigatórios");
            }

            Map<String, Object> resposta = usuarioService.login(email, senha);
            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            // E-mail ou senha inválidos — retorna 401 sem revelar qual dos dois está errado
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalStateException e) {
            // Conta ainda não aprovada
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
