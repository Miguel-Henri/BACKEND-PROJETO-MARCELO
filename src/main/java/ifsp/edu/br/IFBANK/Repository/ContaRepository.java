package ifsp.edu.br.IFBANK.Repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.enums.StatusConta;
import ifsp.edu.br.IFBANK.model.enums.TipoConta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {

    Optional<Conta> findByNumeroContaAndAgencia(Integer numeroConta, Integer agencia);

    Page<Conta> findByStatus(StatusConta status, Pageable pageable);

    Page<Conta> findByRole(TipoConta role, Pageable pageable);

    Page<Conta> findAllByOrderByDataCriacaoDesc(Pageable pageable);

    @Query("SELECT c FROM Conta c WHERE c.status = :status AND c.role = :role ORDER BY c.dataCriacao ASC")
    Page<Conta> findByStatusAndRole(StatusConta status, TipoConta role, Pageable pageable);
}