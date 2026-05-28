package ifsp.edu.br.IFBANK.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.UsuarioService;
import ifsp.edu.br.IFBANK.model.Usuario;

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
    public String criarUser(@RequestBody Usuario usuario) {
        usuarioService.criar(usuario);
        System.out.println("Criando usuário: " + usuario.getNome());
        return usuario.toString();
    }
    
    @GetMapping("/hello") // Shortcut for GET /home/hello
    public String sayHello() {
        return "Hello World!";
    }
}
