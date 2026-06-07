package ifsp.edu.br.IFBANK.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender = null;

    public void sendPasswordResetToken(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Recuperação de senha");
        message.setText("Use o token abaixo para redefinir sua senha:\n\n"
                + token
                + "\n\nEle expira em 15 minutos. Não compartilhe com ninguém.");
        mailSender.send(message);
    }
}