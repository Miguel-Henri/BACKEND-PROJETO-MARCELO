package ifsp.edu.br.IFBANK.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import ifsp.edu.br.IFBANK.model.enums.StatusConta;
import ifsp.edu.br.IFBANK.model.enums.TipoConta;

@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer agencia = 5;

    @Column(name = "numero_conta", nullable = false)
    private Integer numeroConta;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusConta status = StatusConta.PENDENTE;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "contaOrigem")
    private List<Movimentacao> movimentacoesEnviadas;

    @OneToMany(mappedBy = "contaDestino")
    private List<Movimentacao> movimentacoesRecebidas;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Investimento> investimentos;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Emprestimo> emprestimos;

    @Enumerated(EnumType.STRING)
	@Column
	private TipoConta role = TipoConta.CLIENTE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAgencia() {
        return agencia;
    }

    public void setAgencia(Integer agencia) {
        this.agencia = agencia;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoConta getRole() {
        return role;
    }

    public void setRole(TipoConta role) {
        this.role = role  ;
    }

    public StatusConta getStatus() {
        return status;
    }

    public void setStatus(StatusConta status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<Movimentacao> getMovimentacoesEnviadas() {
        return movimentacoesEnviadas;
    }

    public void setMovimentacoesEnviadas(List<Movimentacao> movimentacoesEnviadas) {
        this.movimentacoesEnviadas = movimentacoesEnviadas;
    }

    public List<Movimentacao> getMovimentacoesRecebidas() {
        return movimentacoesRecebidas;
    }

    public void setMovimentacoesRecebidas(List<Movimentacao> movimentacoesRecebidas) {
        this.movimentacoesRecebidas = movimentacoesRecebidas;
    }

    public List<Investimento> getInvestimentos() {
        return investimentos;
    }

    public void setInvestimentos(List<Investimento> investimentos) {
        this.investimentos = investimentos;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}
