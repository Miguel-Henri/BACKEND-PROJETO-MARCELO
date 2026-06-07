package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ifsp.edu.br.IFBANK.model.enums.StatusMovimentacao;
import ifsp.edu.br.IFBANK.model.enums.TipoMovimentacao;

public class MovimentacaoDTO {

    private Integer id;
    private TipoMovimentacao tipo;
    private BigDecimal valor;
    private LocalDateTime dataTransacao;
    private String descricao;
    private StatusMovimentacao status;
    private Integer contaOrigemNumeroConta;
    private Integer contaDestinoNumeroConta;

    public static MovimentacaoDTO from(Movimentacao m) {
        MovimentacaoDTO dto = new MovimentacaoDTO();
        dto.id = m.getId();
        dto.tipo = m.getTipo();
        dto.valor = m.getValor();
        dto.dataTransacao = m.getDataTransacao();
        dto.descricao = m.getDescricao();
        dto.status = m.getStatus();
        dto.contaOrigemNumeroConta = m.getContaOrigem() != null ? m.getContaOrigem().getNumeroConta() : null;
        dto.contaDestinoNumeroConta = m.getContaDestino() != null ? m.getContaDestino().getNumeroConta() : null;
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusMovimentacao getStatus() {
        return status;
    }

    public Integer getContaOrigemNumeroConta() {
        return contaOrigemNumeroConta;
    }

    public Integer getContaDestinoNumeroConta() {
        return contaDestinoNumeroConta;
    }
}
