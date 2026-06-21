package ifsp.edu.br.IFBANK.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.Emprestimo;
import ifsp.edu.br.IFBANK.model.ParcelaEmprestimo;

@Repository
public interface ParcelaEmprestimoRepository extends JpaRepository<ParcelaEmprestimo, Integer> {

    List<ParcelaEmprestimo> findByEmprestimo(Emprestimo emprestimo);

    Optional<ParcelaEmprestimo> findByIdAndEmprestimo(Integer id, Emprestimo emprestimo);
}
