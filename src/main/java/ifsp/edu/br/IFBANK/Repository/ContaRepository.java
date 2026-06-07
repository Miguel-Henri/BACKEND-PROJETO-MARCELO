package ifsp.edu.br.IFBANK.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {
    Optional<Conta> findByNumeroContaAndAgencia(Integer numeroConta, Integer agencia);
}
