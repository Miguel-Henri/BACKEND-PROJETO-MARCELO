package ifsp.edu.br.IFBANK.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ifsp.edu.br.IFBANK.Repository.ResetTokenRepository;
import ifsp.edu.br.IFBANK.Repository.UsuarioRepository;
import ifsp.edu.br.IFBANK.model.TokenRecuperacao;
import ifsp.edu.br.IFBANK.model.Usuario;
import jakarta.transaction.Transactional;

@Service
public class LoginService {

    private final ResetTokenRepository tokenRepository;
    private final UsuarioRepository userRepository;
    private final EmailService emailService;

    // ← Construtor adicionado
    public LoginService(ResetTokenRepository tokenRepository,
                                UsuarioRepository userRepository,
                                EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

 @Transactional
public void requestReset(String email) {
    Usuario usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));

    tokenRepository.deleteByEmail(email);

    TokenRecuperacao resetToken = new TokenRecuperacao();
    resetToken.setEmail(email);
    resetToken.setUsuario(usuario); 

    String codigo = String.format("%06d", new java.util.Random().nextInt(999999));
    resetToken.setToken(codigo);
    resetToken.setDataExpiracao(LocalDateTime.now().plusMinutes(15));

    tokenRepository.save(resetToken);
    emailService.sendPasswordResetToken(email, resetToken.getToken());
}
    

    @Transactional
    public void resetPassword(String token, String newPassword) {
        TokenRecuperacao resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));
        if (resetToken.getDataExpiracao().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token expirado");
        }
        Usuario user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setSenha(newPassword);
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}