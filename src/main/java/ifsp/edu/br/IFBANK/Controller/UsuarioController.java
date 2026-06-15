package ifsp.edu.br.IFBANK.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.ContaService;
import ifsp.edu.br.IFBANK.Service.UsuarioService;
import ifsp.edu.br.IFBANK.model.Usuario;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final ContaService   contaService;
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, ContaService contaService) {
        this.usuarioService = usuarioService;
        this.contaService   = contaService;
    }

    /** POST /api/usuarios/novo */
    @PostMapping("/novo")
    public ResponseEntity<Void> criarUser(@RequestBody Usuario usuario) {
        usuarioService.criar(usuario);
        contaService.criar(usuario);
        return ResponseEntity.status(201).build();
    }

    /** GET /api/usuarios/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    /** PATCH /api/usuarios/{id} */
    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Integer id,
            @RequestBody Map<String, String> dados) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dados));
    }

    /** POST /api/usuarios/login */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");
        return ResponseEntity.ok(usuarioService.login(email, senha));
    }
}
