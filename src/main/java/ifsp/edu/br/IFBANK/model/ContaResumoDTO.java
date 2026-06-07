package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ifsp.edu.br.IFBANK.model.enums.StatusConta;
import ifsp.edu.br.IFBANK.model.enums.TipoConta;

public class ContaResumoDTO {

    private Integer id;
    private Integer numeroConta;
    private Integer agencia;
    private TipoConta tipo;
    private StatusConta status;
    private BigDecimal saldo;
    private LocalDateTime dataCriacao;

    // Dados do usuário dono da conta
    private Integer usuarioId;
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;
    private String enderecoUsuario;
    private String fotoPerfil;

    // Construtor vazio obrigatório para Jackson
    public ContaResumoDTO() {}

    /**
     * Constrói um DTO a partir de uma entidade Conta.
     * Deve ser chamado com a conta já com o usuário carregado.
     */
    public static ContaResumoDTO from(Conta conta) {
        ContaResumoDTO dto = new ContaResumoDTO();
        dto.setId(conta.getId());
        dto.setNumeroConta(conta.getNumeroConta());
        dto.setAgencia(conta.getAgencia());
        dto.setTipo(conta.getTipo());
        dto.setStatus(conta.getStatus());
        dto.setSaldo(conta.getSaldo());
        dto.setDataCriacao(conta.getDataCriacao());

        if (conta.getUsuario() != null) {
            Usuario u = conta.getUsuario();
            dto.setUsuarioId(u.getId());
            dto.setNomeUsuario(u.getNome());
            dto.setEmailUsuario(u.getEmail());
            dto.setTelefoneUsuario(u.getTelefone());
            dto.setEnderecoUsuario(u.getEndereco());
            dto.setFotoPerfil(u.getFotoPerfil());
        }

        return dto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Integer getAgencia() {
        return agencia;
    }

    public void setAgencia(Integer agencia) {
        this.agencia = agencia;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public StatusConta getStatus() {
        return status;
    }

    public void setStatus(StatusConta status) {
        this.status = status;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTelefoneUsuario() {
        return telefoneUsuario;
    }

    public void setTelefoneUsuario(String telefoneUsuario) {
        this.telefoneUsuario = telefoneUsuario;
    }

    public String getEnderecoUsuario() {
        return enderecoUsuario;
    }

    public void setEnderecoUsuario(String enderecoUsuario) {
        this.enderecoUsuario = enderecoUsuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
