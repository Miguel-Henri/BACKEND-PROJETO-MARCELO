package ifsp.edu.br.IFBANK.Controller;

import ifsp.edu.br.IFBANK.Service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/auth/")
public class LoginController {
    
    private final LoginService loginService;
        public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    public void postMethodName(@RequestParam String email, RequestParam senha) {
        
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