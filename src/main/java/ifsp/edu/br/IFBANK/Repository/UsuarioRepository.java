package ifsp.edu.br.IFBANK.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Usado no login para buscar o usuário pelo e-mail
    Optional<Usuario> findByEmail(String email);

    // Verifica se já existe um usuário com determinado e-mail (útil para cadastro)
    boolean existsByEmail(String email);
}
