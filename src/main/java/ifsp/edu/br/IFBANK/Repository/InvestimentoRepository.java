package ifsp.edu.br.IFBANK.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.Investimento;
import ifsp.edu.br.IFBANK.model.enums.StatusInvestimento;

@Repository
public interface InvestimentoRepository extends JpaRepository<Investimento, Integer> {

    List<Investimento> findByConta(Conta conta);

    List<Investimento> findByContaAndStatus(Conta conta, StatusInvestimento status);

    Optional<Investimento> findByIdAndConta(Integer id, Conta conta);
}
