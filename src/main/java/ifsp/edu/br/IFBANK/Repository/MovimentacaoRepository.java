package ifsp.edu.br.IFBANK.Repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.Movimentacao;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {

    @Query("SELECT m FROM Movimentacao m WHERE m.dataTransacao BETWEEN :inicio AND :fim AND (" +
           "(m.tipo = ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao.TRANSFERENCIA_ENVIADA AND m.contaOrigem = :conta) OR " +
           "(m.tipo = ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao.TRANSFERENCIA_RECEBIDA AND m.contaDestino = :conta) OR " +
           "(m.tipo NOT IN (ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao.TRANSFERENCIA_ENVIADA, ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao.TRANSFERENCIA_RECEBIDA) AND (m.contaOrigem = :conta OR m.contaDestino = :conta))" +
           ") ORDER BY m.dataTransacao DESC")
    Page<Movimentacao> findByContaAndPeriodo(
        @Param("conta") Conta conta,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        Pageable pageable
    );
}
