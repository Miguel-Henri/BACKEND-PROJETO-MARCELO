package ifsp.edu.br.IFBANK.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.ConfigEmprestimo;

@Repository
public interface ConfigEmprestimoRepository extends JpaRepository<ConfigEmprestimo, Integer> {
}
