package ifsp.edu.br.IFBANK.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.UsuarioService;
import ifsp.edu.br.IFBANK.model.Usuario;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

}
