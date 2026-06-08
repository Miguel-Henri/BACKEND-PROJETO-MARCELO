package ifsp.edu.br.IFBANK.model;

public class GerenteAcaoRequest {

    // ID da conta que será aprovada ou rejeitada
    private Integer contaId;

    // "APROVAR" ou "REJEITAR"
    private String acao;

    // ID da conta do gerente que está executando a ação (usado para validação de perfil)
    private Integer gerenteContaId;

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

    public Integer getGerenteContaId() {
        return gerenteContaId;
    }

    public void setGerenteContaId(Integer gerenteContaId) {
        this.gerenteContaId = gerenteContaId;
    }
}
