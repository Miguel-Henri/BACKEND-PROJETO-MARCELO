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

    // Busca contas pelo status (ex: todas as PENDENTES para o gerente aprovar)
    Page<Conta> findByStatus(StatusConta status, Pageable pageable);

    // Busca contas por tipo (ex: todas as contas CLIENTE)
    Page<Conta> findByTipo(TipoConta tipo, Pageable pageable);

    // Lista todas as contas ordenadas por data de criação (visão geral do gerente)
    Page<Conta> findAllByOrderByDataCriacaoDesc(Pageable pageable);

    // Busca contas PENDENTES que são do tipo CLIENTE (ignora contas de gerente)
    @Query("SELECT c FROM Conta c WHERE c.status = :status AND c.tipo = :tipo ORDER BY c.dataCriacao ASC")
    Page<Conta> findByStatusAndTipo(StatusConta status, TipoConta tipo, Pageable pageable);
}