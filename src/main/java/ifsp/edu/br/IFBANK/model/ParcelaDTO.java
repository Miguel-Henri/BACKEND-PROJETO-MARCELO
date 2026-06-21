package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ifsp.edu.br.IFBANK.model.enums.StatusParcela;

public class ParcelaDTO {

    private Integer id;
    private Integer numeroParcela;
    private BigDecimal valorParcela;
    private BigDecimal valorJuros;
    private BigDecimal valorAmortizacao;
    private LocalDateTime dataVencimento;
    private LocalDateTime dataPagamento;
    private StatusParcela status;

    public static ParcelaDTO from(ParcelaEmprestimo p) {
        ParcelaDTO dto = new ParcelaDTO();
        dto.id = p.getId();
        dto.numeroParcela = p.getNumeroParcela();
        dto.valorParcela = p.getValorParcela();
        dto.valorJuros = p.getValorJuros();
        dto.valorAmortizacao = p.getValorAmortizacao();
        dto.dataVencimento = p.getDataVencimento();
        dto.dataPagamento = p.getDataPagamento();
        dto.status = p.getStatus();
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public BigDecimal getValorJuros() {
        return valorJuros;
    }

    public BigDecimal getValorAmortizacao() {
        return valorAmortizacao;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public StatusParcela getStatus() {
        return status;
    }
}
