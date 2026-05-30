package ifsp.edu.br.IFBANK.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.Usuario;

@Repository
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
}
