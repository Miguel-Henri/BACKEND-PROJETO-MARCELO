package ifsp.edu.br.IFBANK.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ifsp.edu.br.IFBANK.Repository.ContaRepository;
import ifsp.edu.br.IFBANK.Repository.MovimentacaoRepository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.DepositoRequest;
import ifsp.edu.br.IFBANK.model.Movimentacao;
import ifsp.edu.br.IFBANK.model.MovimentacaoDTO;
import ifsp.edu.br.IFBANK.model.TransferenciaRequest;
import ifsp.edu.br.IFBANK.model.Usuario;
import ifsp.edu.br.IFBANK.model.enums.StatusMovimentacao;
import ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public ContaService(ContaRepository contaRepository, MovimentacaoRepository movimentacaoRepository) {
        this.contaRepository = contaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }



    public void criar(Usuario usuario) {

        Conta conta = new Conta();
        conta.setUsuario(usuario);
        conta.setAgencia(5); // ← agência predefinida
        conta.setNumeroConta(10000+usuario.getId());
        contaRepository.save(conta);
    }

    @Transactional
    public void transferir(TransferenciaRequest request) {
        Conta contaOrigem = contaRepository
            .findByNumeroContaAndAgencia(request.getNumeroContaOrigem(), request.getAgenciaOrigem())
            .orElseThrow(() -> new NoSuchElementException("Conta de origem não encontrada"));

        Conta contaDestino = contaRepository
            .findByNumeroContaAndAgencia(request.getNumeroContaDestino(), request.getAgenciaDestino())
            .orElseThrow(() -> new NoSuchElementException("Conta de destino não encontrada"));

        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new IllegalArgumentException("Conta de origem e destino não podem ser a mesma");
        }

        if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (contaOrigem.getSaldo().compareTo(request.getValor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        Movimentacao movEnviada = new Movimentacao();
        movEnviada.setContaOrigem(contaOrigem);
        movEnviada.setContaDestino(contaDestino);
        movEnviada.setValor(request.getValor());
        movEnviada.setSaldoAnterior(contaOrigem.getSaldo());
        movEnviada.setSaldoPosterior(contaOrigem.getSaldo().subtract(request.getValor()));
        movEnviada.setTipo(TipoMovimentacao.TRANSFERENCIA_ENVIADA);
        movEnviada.setDescricao(request.getDescricao());
        movEnviada.setStatus(StatusMovimentacao.PENDENTE);

        Movimentacao movRecebida = new Movimentacao();
        movRecebida.setContaOrigem(contaOrigem);
        movRecebida.setContaDestino(contaDestino);
        movRecebida.setValor(request.getValor());
        movRecebida.setSaldoAnterior(contaDestino.getSaldo());
        movRecebida.setSaldoPosterior(contaDestino.getSaldo().add(request.getValor()));
        movRecebida.setTipo(TipoMovimentacao.TRANSFERENCIA_RECEBIDA);
        movRecebida.setDescricao(request.getDescricao());
        movRecebida.setStatus(StatusMovimentacao.PENDENTE);

        movimentacaoRepository.save(movEnviada);
        movimentacaoRepository.save(movRecebida);

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.getValor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.getValor()));
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        movEnviada.setStatus(StatusMovimentacao.CONCLUIDA);
        movRecebida.setStatus(StatusMovimentacao.CONCLUIDA);
        movimentacaoRepository.save(movEnviada);
        movimentacaoRepository.save(movRecebida);
    }

    @Transactional
    public void depositar(DepositoRequest request) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(request.getNumeroConta(), request.getAgencia())
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        Movimentacao mov = new Movimentacao();
        mov.setContaDestino(conta);
        mov.setValor(request.getValor());
        mov.setSaldoAnterior(conta.getSaldo());
        mov.setSaldoPosterior(conta.getSaldo().add(request.getValor()));
        mov.setTipo(TipoMovimentacao.DEPOSITO);
        mov.setDescricao("Depósito");
        mov.setStatus(StatusMovimentacao.PENDENTE);

        movimentacaoRepository.save(mov);

        conta.setSaldo(conta.getSaldo().add(request.getValor()));
        contaRepository.save(conta);

        mov.setStatus(StatusMovimentacao.CONCLUIDA);
        movimentacaoRepository.save(mov);
    }

    public Page<MovimentacaoDTO> extrato(Integer numeroConta, Integer agencia, LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(numeroConta, agencia)
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        return movimentacaoRepository
            .findByContaAndPeriodo(conta, inicio, fim, pageable)
            .map(MovimentacaoDTO::from);
    }
}
