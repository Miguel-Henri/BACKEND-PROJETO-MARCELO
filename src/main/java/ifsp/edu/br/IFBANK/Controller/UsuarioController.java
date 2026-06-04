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

    @PostMapping("/novo")
    public ResponseEntity<String> criarUser(@RequestBody Usuario usuario) {
        try {
            usuarioService.criar(usuario);
            return ResponseEntity.status(201).build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("E-mail já cadastrado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar usuário.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

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
}
