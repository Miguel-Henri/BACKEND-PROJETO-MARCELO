package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;

public class TransferenciaRequest {

    private Integer agenciaOrigem;
    private Integer numeroContaOrigem;
    private Integer agenciaDestino;
    private Integer numeroContaDestino;
    private BigDecimal valor;
    private String descricao;

    public Integer getAgenciaOrigem() {
        return agenciaOrigem;
    }

    public void setAgenciaOrigem(Integer agenciaOrigem) {
        this.agenciaOrigem = agenciaOrigem;
    }

    public Integer getNumeroContaOrigem() {
        return numeroContaOrigem;
    }

    public void setNumeroContaOrigem(Integer numeroContaOrigem) {
        this.numeroContaOrigem = numeroContaOrigem;
    }

    public Integer getAgenciaDestino() {
        return agenciaDestino;
    }

    public void setAgenciaDestino(Integer agenciaDestino) {
        this.agenciaDestino = agenciaDestino;
    }

    public Integer getNumeroContaDestino() {
        return numeroContaDestino;
    }

    public void setNumeroContaDestino(Integer numeroContaDestino) {
        this.numeroContaDestino = numeroContaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
