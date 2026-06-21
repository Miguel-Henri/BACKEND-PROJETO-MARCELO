package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ifsp.edu.br.IFBANK.model.enums.StatusInvestimento;

public class InvestimentoDTO {

    private Integer id;
    private String tipoInvestimento;
    private BigDecimal valorInvestido;
    private BigDecimal valorAtual;
    private BigDecimal rendimento;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private StatusInvestimento status;

    public static InvestimentoDTO from(Investimento investimento, BigDecimal valorAtual, BigDecimal rendimento) {
        InvestimentoDTO dto = new InvestimentoDTO();
        dto.id = investimento.getId();
        dto.tipoInvestimento = investimento.getTipoInvestimento();
        dto.valorInvestido = investimento.getValorInvestido();
        dto.valorAtual = valorAtual;
        dto.rendimento = rendimento;
        dto.dataInicio = investimento.getDataInicio();
        dto.dataFim = investimento.getDataFim();
        dto.status = investimento.getStatus();
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public String getTipoInvestimento() {
        return tipoInvestimento;
    }

    public BigDecimal getValorInvestido() {
        return valorInvestido;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public BigDecimal getRendimento() {
        return rendimento;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public StatusInvestimento getStatus() {
        return status;
    }
}
