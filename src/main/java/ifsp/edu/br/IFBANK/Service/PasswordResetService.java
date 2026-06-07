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
@RequiredArgsConstructor
public class PasswordResetService {

    private  ResetTokenRepository tokenRepository;
    private  UsuarioRepository userRepository;
    private  EmailService emailService;

    // PASSO 1 — Recebe email, gera token, envia email
    @Transactional
    public void requestReset(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));

        // Remove token anterior se existir
        tokenRepository.deleteByEmail(email);

        TokenRecuperacao resetToken = new TokenRecuperacao();
        resetToken.setEmail(email);
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setDataExpiracao(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        emailService.sendPasswordResetToken(email, resetToken.getToken());
    }

    // PASSO 2 — Valida token e troca a senha
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

        // Remove o token após uso
        tokenRepository.delete(resetToken);
    }
}