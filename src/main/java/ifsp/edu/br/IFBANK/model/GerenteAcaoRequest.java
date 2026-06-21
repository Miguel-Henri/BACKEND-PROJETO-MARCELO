package ifsp.edu.br.IFBANK.model;

public class GerenteAcaoRequest {

    // ID da conta que será aprovada ou rejeitada
    private Integer contaId;

    // "APROVAR" ou "REJEITAR"
    private String acao;

    public Integer getContaId() {
        return contaId;
    }

    public void setContaId(Integer contaId) {
        this.contaId = contaId;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
}
