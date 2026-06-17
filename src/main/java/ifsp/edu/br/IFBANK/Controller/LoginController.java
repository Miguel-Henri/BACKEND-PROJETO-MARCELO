package ifsp.edu.br.IFBANK.Controller;

import ifsp.edu.br.IFBANK.Config.JwtService;
import ifsp.edu.br.IFBANK.Service.LoginService;
import ifsp.edu.br.IFBANK.model.LoginRequestDTO;
import ifsp.edu.br.IFBANK.model.LoginResponseDTO;
import ifsp.edu.br.IFBANK.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
        public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> postMethodName(@RequestBody LoginRequestDTO request) {
          Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String token = jwtService.gerarToken(usuario);
        
        return ResponseEntity.ok(new LoginResponseDTO(token));
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