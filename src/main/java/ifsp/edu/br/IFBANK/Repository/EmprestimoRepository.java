package ifsp.edu.br.IFBANK.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.Emprestimo;
import ifsp.edu.br.IFBANK.model.enums.StatusEmprestimo;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {

    List<Emprestimo> findByConta(Conta conta);

    Optional<Emprestimo> findByIdAndConta(Integer id, Conta conta);

    List<Emprestimo> findByStatus(StatusEmprestimo status);
}
