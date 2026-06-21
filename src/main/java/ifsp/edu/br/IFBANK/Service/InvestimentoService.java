package ifsp.edu.br.IFBANK.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ifsp.edu.br.IFBANK.Repository.ContaRepository;
import ifsp.edu.br.IFBANK.Repository.InvestimentoRepository;
import ifsp.edu.br.IFBANK.Repository.MovimentacaoRepository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.Investimento;
import ifsp.edu.br.IFBANK.model.InvestimentoDTO;
import ifsp.edu.br.IFBANK.model.InvestimentoRequest;
import ifsp.edu.br.IFBANK.model.Movimentacao;
import ifsp.edu.br.IFBANK.model.enums.StatusInvestimento;
import ifsp.edu.br.IFBANK.model.enums.StatusMovimentacao;
import ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao;

@Service
public class InvestimentoService {

    private static final Map<String, BigDecimal> TAXAS_MENSAIS = Map.of(
        "CDB", new BigDecimal("0.009"),
        "LCI", new BigDecimal("0.008"),
        "LCA", new BigDecimal("0.008"),
        "TESOURO_SELIC", new BigDecimal("0.010"),
        "TESOURO_PREFIXADO", new BigDecimal("0.011")
    );

    private static final BigDecimal TAXA_PADRAO = new BigDecimal("0.008");

    private final ContaRepository contaRepository;
    private final InvestimentoRepository investimentoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public InvestimentoService(ContaRepository contaRepository,
                               InvestimentoRepository investimentoRepository,
                               MovimentacaoRepository movimentacaoRepository) {
        this.contaRepository = contaRepository;
        this.investimentoRepository = investimentoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public InvestimentoDTO aplicar(InvestimentoRequest request) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(request.getNumeroConta(), request.getAgencia())
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        if (request.getValorInvestido() == null || request.getValorInvestido().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do investimento deve ser maior que zero");
        }

        if (request.getTipoInvestimento() == null || request.getTipoInvestimento().isBlank()) {
            throw new IllegalArgumentException("Tipo de investimento é obrigatório");
        }

        if (conta.getSaldo().compareTo(request.getValorInvestido()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o investimento");
        }

        // Debita o saldo da conta
        conta.setSaldo(conta.getSaldo().subtract(request.getValorInvestido()));
        contaRepository.save(conta);

        // Registra movimentação
        Movimentacao mov = new Movimentacao();
        mov.setContaOrigem(conta);
        mov.setValor(request.getValorInvestido());
        mov.setSaldoAnterior(conta.getSaldo().add(request.getValorInvestido()));
        mov.setSaldoPosterior(conta.getSaldo());
        mov.setTipo(TipoMovimentacao.INVESTIMENTO);
        mov.setDescricao("Investimento aplicado: " + request.getTipoInvestimento());
        mov.setStatus(StatusMovimentacao.CONCLUIDA);
        movimentacaoRepository.save(mov);

        // Cria o investimento
        Investimento investimento = new Investimento();
        investimento.setConta(conta);
        investimento.setTipoInvestimento(request.getTipoInvestimento());
        investimento.setValorInvestido(request.getValorInvestido());
        investimento.setDataInicio(LocalDateTime.now());
        investimento.setStatus(StatusInvestimento.ATIVO);

        return montarDTO(investimentoRepository.save(investimento));
    }

    @Transactional
    public List<InvestimentoDTO> listar(Integer numeroConta, Integer agencia) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(numeroConta, agencia)
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        List<InvestimentoDTO> lista = new ArrayList<>();
        for (Investimento investimento : investimentoRepository.findByConta(conta)) {
            lista.add(montarDTO(investimento));
        }
        return lista;
    }

    @Transactional
    public InvestimentoDTO encerrar(Integer investimentoId, Integer numeroConta, Integer agencia) {
        Conta conta = contaRepository
            .findByNumeroContaAndAgencia(numeroConta, agencia)
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada"));

        Investimento investimento = investimentoRepository
            .findByIdAndConta(investimentoId, conta)
            .orElseThrow(() -> new NoSuchElementException("Investimento não encontrado para esta conta"));

        if (investimento.getStatus() == StatusInvestimento.ENCERRADO) {
            throw new IllegalArgumentException("Investimento já está encerrado");
        }

        BigDecimal valorResgate = calcularValorAtual(investimento);

        // Devolve o valor atual ao saldo da conta
        conta.setSaldo(conta.getSaldo().add(valorResgate));
        contaRepository.save(conta);

        // Registra movimentação de retorno
        Movimentacao mov = new Movimentacao();
        mov.setContaDestino(conta);
        mov.setValor(valorResgate);
        mov.setSaldoAnterior(conta.getSaldo().subtract(valorResgate));
        mov.setSaldoPosterior(conta.getSaldo());
        mov.setTipo(TipoMovimentacao.INVESTIMENTO);
        mov.setDescricao("Investimento encerrado: " + investimento.getTipoInvestimento());
        mov.setStatus(StatusMovimentacao.CONCLUIDA);
        movimentacaoRepository.save(mov);

        // Encerra o investimento
        investimento.setStatus(StatusInvestimento.ENCERRADO);
        investimento.setDataFim(LocalDateTime.now());

        return montarDTO(investimentoRepository.save(investimento));
    }

    private InvestimentoDTO montarDTO(Investimento investimento) {
        BigDecimal valorAtual = calcularValorAtual(investimento);
        BigDecimal rendimento = valorAtual.subtract(investimento.getValorInvestido());
        return InvestimentoDTO.from(investimento, valorAtual, rendimento);
    }

    private BigDecimal calcularValorAtual(Investimento investimento) {
        BigDecimal taxaMensal = TAXAS_MENSAIS.getOrDefault(investimento.getTipoInvestimento(), TAXA_PADRAO);

        LocalDateTime inicio = investimento.getDataInicio() != null ? investimento.getDataInicio() : LocalDateTime.now();
        LocalDateTime fim = investimento.getDataFim() != null ? investimento.getDataFim() : LocalDateTime.now();
        long dias = ChronoUnit.DAYS.between(inicio, fim);
        if (dias < 0) {
            dias = 0;
        }

        BigDecimal meses = new BigDecimal(dias).divide(new BigDecimal("30"), 6, RoundingMode.HALF_UP);
        BigDecimal fator = BigDecimal.ONE.add(taxaMensal.multiply(meses));

        return investimento.getValorInvestido().multiply(fator).setScale(2, RoundingMode.HALF_UP);
    }
}
