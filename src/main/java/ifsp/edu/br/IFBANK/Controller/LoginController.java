package ifsp.edu.br.IFBANK.Controller;

import ifsp.edu.br.IFBANK.Config.JwtService;
import ifsp.edu.br.IFBANK.Exception.CredenciaisInvalidasException;
import ifsp.edu.br.IFBANK.Service.LoginService;
import ifsp.edu.br.IFBANK.Service.UsuarioService;
import ifsp.edu.br.IFBANK.model.LoginRequestDTO;
import ifsp.edu.br.IFBANK.model.Usuario;
import ifsp.edu.br.IFBANK.model.enums.StatusUsuario;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private final LoginService loginService;
    private final UsuarioService usuarioService;

    public LoginController(LoginService loginService, UsuarioService usuarioService) {
        this.loginService = loginService;
        this.usuarioService = usuarioService;
    }


@PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO request) {
    Authentication authentication;
    
    try {
        authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
    } catch (BadCredentialsException e) {
        throw new CredenciaisInvalidasException("E-mail ou senha inválidos");
    }

    Usuario usuario = (Usuario) authentication.getPrincipal();

    if (!usuario.getStatus().equals(StatusUsuario.ATIVO)) {
        throw new CredenciaisInvalidasException("Sua conta ainda não foi aprovada. Procure o gerente.");
    }

    Map<String, Object> resposta = usuarioService.dadosSessao(usuario);
    resposta.put("token", jwtService.gerarToken(usuario));
    return ResponseEntity.ok(resposta);
}
    
    


    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        loginService.requestReset(email);
        return ResponseEntity.ok("E-mail de recuperação enviado com sucesso!");
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        loginService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }


}