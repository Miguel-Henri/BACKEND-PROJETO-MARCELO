package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import ifsp.edu.br.IFBANK.model.enums.StatusEmprestimo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "emprestimos",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_emprestimo_duplicado",
        columnNames = {"conta_id", "valor_emprestimo", "parcelas", "taxa_juros_mensal"}
    )
)
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Column(name = "valor_emprestimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorEmprestimo;

    @Column(name = "taxa_juros_mensal", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJurosMensal;

    @Column(nullable = false)
    private Integer parcelas;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private StatusEmprestimo status = StatusEmprestimo.SIMULADO;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "data_ultimo_pagamento")
    private LocalDateTime dataUltimoPagamento;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL)
    private List<ParcelaEmprestimo> parcelasEmprestimo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public BigDecimal getValorEmprestimo() {
		return valorEmprestimo;
	}

	public void setValorEmprestimo(BigDecimal valorEmprestimo) {
		this.valorEmprestimo = valorEmprestimo;
	}

	public BigDecimal getTaxaJurosMensal() {
		return taxaJurosMensal;
	}

	public void setTaxaJurosMensal(BigDecimal taxaJurosMensal) {
		this.taxaJurosMensal = taxaJurosMensal;
	}

	public Integer getParcelas() {
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	public StatusEmprestimo getStatus() {
		return status;
	}

	public void setStatus(StatusEmprestimo status) {
		this.status = status;
	}

	public LocalDateTime getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public LocalDateTime getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(LocalDateTime dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public LocalDateTime getDataUltimoPagamento() {
		return dataUltimoPagamento;
	}

	public void setDataUltimoPagamento(LocalDateTime dataUltimoPagamento) {
		this.dataUltimoPagamento = dataUltimoPagamento;
	}

	public List<ParcelaEmprestimo> getParcelasEmprestimo() {
		return parcelasEmprestimo;
	}

	public void setParcelasEmprestimo(List<ParcelaEmprestimo> parcelasEmprestimo) {
		this.parcelasEmprestimo = parcelasEmprestimo;
	}
    
    
    
}
