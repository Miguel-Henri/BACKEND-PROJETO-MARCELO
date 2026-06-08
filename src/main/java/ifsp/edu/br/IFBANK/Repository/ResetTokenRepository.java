package ifsp.edu.br.IFBANK.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ifsp.edu.br.IFBANK.model.TokenRecuperacao;

public interface ResetTokenRepository extends JpaRepository<TokenRecuperacao, Integer> {
    Optional<TokenRecuperacao> findByToken(String token);
    void deleteByEmail(String email);
    
}
