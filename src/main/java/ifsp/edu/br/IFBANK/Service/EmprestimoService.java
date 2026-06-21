package ifsp.edu.br.IFBANK.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ifsp.edu.br.IFBANK.Repository.ConfigEmprestimoRepository;
import ifsp.edu.br.IFBANK.Repository.ContaRepository;
import ifsp.edu.br.IFBANK.Repository.EmprestimoRepository;
import ifsp.edu.br.IFBANK.Repository.MovimentacaoRepository;
import ifsp.edu.br.IFBANK.Repository.ParcelaEmprestimoRepository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.Emprestimo;
import ifsp.edu.br.IFBANK.model.EmprestimoDTO;
import ifsp.edu.br.IFBANK.model.EmprestimoRequest;
import ifsp.edu.br.IFBANK.model.Movimentacao;
import ifsp.edu.br.IFBANK.model.ParcelaEmprestimo;
import ifsp.edu.br.IFBANK.model.enums.StatusEmprestimo;
import ifsp.edu.br.IFBANK.model.enums.StatusMovimentacao;
import ifsp.edu.br.IFBANK.model.enums.StatusParcela;
import ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao;

@Service
public class EmprestimoService {

    private static final BigDecimal TAXA_PADRAO = new BigDecimal("2.50");

    private final ContaRepository contaRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final ParcelaEmprestimoRepository parcelaEmprestimoRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ConfigEmprestimoRepository configEmprestimoRepository;

    public EmprestimoService(ContaRepository contaRepository,
                             EmprestimoRepository emprestimoRepository,
                             ParcelaEmprestimoRepository parcelaEmprestimoRepository,
                             MovimentacaoRepository movimentacaoRepository,
                             ConfigEmprestimoRepository configEmprestimoRepository) {
        this.contaRepository = contaRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.parcelaEmprestimoRepository = parcelaEmprestimoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.configEmprestimoRepository = configEmprestimoRepository;
    }

    private BigDecimal taxaJurosAtual() {
        return configEmprestimoRepository.findAll().stream()
            .findFirst()
            .map(config -> config.getTaxaJurosPadrao())
            .orElse(TAXA_PADRAO);
    }

    @Transactional
    public EmprestimoDTO solicitar(EmprestimoRequest request) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(request.getNumeroConta(), request.getAgencia())
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        if (request.getValorEmprestimo() == null || request.getValorEmprestimo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do empréstimo deve ser maior que zero");
        }

        if (request.getParcelas() == null || request.getParcelas() <= 0) {
            throw new IllegalArgumentException("Número de parcelas deve ser maior que zero");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setConta(conta);
        emprestimo.setValorEmprestimo(request.getValorEmprestimo());
        emprestimo.setParcelas(request.getParcelas());
        emprestimo.setTaxaJurosMensal(taxaJurosAtual());
        emprestimo.setStatus(StatusEmprestimo.SOLICITADO);
        emprestimo.setDataSolicitacao(LocalDateTime.now());

        return EmprestimoDTO.from(emprestimoRepository.save(emprestimo));
    }

    @Transactional
    public List<EmprestimoDTO> listar(Integer numeroConta, Integer agencia) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(numeroConta, agencia)
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        List<EmprestimoDTO> lista = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimoRepository.findByConta(conta)) {
            lista.add(EmprestimoDTO.from(emprestimo));
        }
        return lista;
    }

    @Transactional
    public List<EmprestimoDTO> listarPendentes() {
        List<EmprestimoDTO> lista = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimoRepository.findByStatus(StatusEmprestimo.SOLICITADO)) {
            lista.add(EmprestimoDTO.from(emprestimo));
        }
        return lista;
    }

    @Transactional
    public EmprestimoDTO aprovar(Integer emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
            .orElseThrow(() -> new NoSuchElementException("Empréstimo não encontrado"));

        if (emprestimo.getStatus() != StatusEmprestimo.SOLICITADO) {
            throw new IllegalArgumentException("Empréstimo não está pendente de aprovação");
        }

        Conta conta = emprestimo.getConta();
        emprestimo.setStatus(StatusEmprestimo.EM_ANDAMENTO);
        emprestimo.setDataAprovacao(LocalDateTime.now());

        conta.setSaldo(conta.getSaldo().add(emprestimo.getValorEmprestimo()));
        contaRepository.save(conta);

        Movimentacao mov = new Movimentacao();
        mov.setContaDestino(conta);
        mov.setValor(emprestimo.getValorEmprestimo());
        mov.setSaldoAnterior(conta.getSaldo().subtract(emprestimo.getValorEmprestimo()));
        mov.setSaldoPosterior(conta.getSaldo());
        mov.setTipo(TipoMovimentacao.EMPRESTIMO);
        mov.setDescricao("Empréstimo aprovado");
        mov.setStatus(StatusMovimentacao.CONCLUIDA);
        movimentacaoRepository.save(mov);

        emprestimo.setParcelasEmprestimo(gerarParcelas(emprestimo));

        return EmprestimoDTO.from(emprestimoRepository.save(emprestimo));
    }

    @Transactional
    public EmprestimoDTO rejeitar(Integer emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
            .orElseThrow(() -> new NoSuchElementException("Empréstimo não encontrado"));

        if (emprestimo.getStatus() != StatusEmprestimo.SOLICITADO) {
            throw new IllegalArgumentException("Empréstimo não está pendente de aprovação");
        }

        emprestimo.setStatus(StatusEmprestimo.REJEITADO);
        return EmprestimoDTO.from(emprestimoRepository.save(emprestimo));
    }

    @Transactional
    public EmprestimoDTO pagarParcela(Integer emprestimoId, Integer parcelaId, Integer numeroConta, Integer agencia) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(numeroConta, agencia)
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        Emprestimo emprestimo = emprestimoRepository.findByIdAndConta(emprestimoId, conta)
            .orElseThrow(() -> new NoSuchElementException("Empréstimo não encontrado para esta conta"));

        ParcelaEmprestimo parcela = parcelaEmprestimoRepository.findByIdAndEmprestimo(parcelaId, emprestimo)
            .orElseThrow(() -> new NoSuchElementException("Parcela não encontrada para este empréstimo"));

        if (parcela.getStatus() == StatusParcela.PAGO) {
            throw new IllegalArgumentException("Parcela já está paga");
        }

        if (conta.getSaldo().compareTo(parcela.getValorParcela()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para pagar a parcela");
        }

        conta.setSaldo(conta.getSaldo().subtract(parcela.getValorParcela()));
        contaRepository.save(conta);

        Movimentacao mov = new Movimentacao();
        mov.setContaOrigem(conta);
        mov.setValor(parcela.getValorParcela());
        mov.setSaldoAnterior(conta.getSaldo().add(parcela.getValorParcela()));
        mov.setSaldoPosterior(conta.getSaldo());
        mov.setTipo(TipoMovimentacao.PAGAMENTO);
        mov.setDescricao("Pagamento da parcela " + parcela.getNumeroParcela() + " do empréstimo");
        mov.setStatus(StatusMovimentacao.CONCLUIDA);
        movimentacaoRepository.save(mov);

        parcela.setStatus(StatusParcela.PAGO);
        parcela.setDataPagamento(LocalDateTime.now());
        parcelaEmprestimoRepository.save(parcela);

        emprestimo.setDataUltimoPagamento(LocalDateTime.now());
        if (todasParcelasPagas(emprestimo)) {
            emprestimo.setStatus(StatusEmprestimo.QUITADO);
        }

        return EmprestimoDTO.from(emprestimoRepository.save(emprestimo));
    }

    private boolean todasParcelasPagas(Emprestimo emprestimo) {
        for (ParcelaEmprestimo parcela : parcelaEmprestimoRepository.findByEmprestimo(emprestimo)) {
            if (parcela.getStatus() != StatusParcela.PAGO) {
                return false;
            }
        }
        return true;
    }

    private List<ParcelaEmprestimo> gerarParcelas(Emprestimo emprestimo) {
        BigDecimal valor = emprestimo.getValorEmprestimo();
        BigDecimal taxa = emprestimo.getTaxaJurosMensal();
        int quantidade = emprestimo.getParcelas();

        BigDecimal fatorJuros = taxa.divide(new BigDecimal("100")).multiply(new BigDecimal(quantidade));
        BigDecimal total = valor.multiply(BigDecimal.ONE.add(fatorJuros));

        BigDecimal valorParcela = total.divide(new BigDecimal(quantidade), 2, RoundingMode.HALF_UP);
        BigDecimal valorAmortizacao = valor.divide(new BigDecimal(quantidade), 2, RoundingMode.HALF_UP);
        BigDecimal valorJuros = valorParcela.subtract(valorAmortizacao);

        List<ParcelaEmprestimo> parcelas = new ArrayList<>();
        for (int numero = 1; numero <= quantidade; numero++) {
            ParcelaEmprestimo parcela = new ParcelaEmprestimo();
            parcela.setEmprestimo(emprestimo);
            parcela.setNumeroParcela(numero);
            parcela.setValorParcela(valorParcela);
            parcela.setValorAmortizacao(valorAmortizacao);
            parcela.setValorJuros(valorJuros);
            parcela.setDataVencimento(emprestimo.getDataAprovacao().plusMonths(numero));
            parcela.setStatus(StatusParcela.PENDENTE);
            parcelas.add(parcelaEmprestimoRepository.save(parcela));
        }
        return parcelas;
    }
}
