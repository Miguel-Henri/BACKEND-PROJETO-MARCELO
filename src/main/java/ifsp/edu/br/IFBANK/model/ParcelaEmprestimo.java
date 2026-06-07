package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ifsp.edu.br.IFBANK.model.enums.StatusParcela;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parcelas_emprestimo")
public class ParcelaEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "emprestimo_id", nullable = false)
    private Emprestimo emprestimo;

    @Column(name = "numero_parcela", nullable = false)
    private Integer numeroParcela;

    @Column(name = "valor_amortizacao", precision = 15, scale = 2)
    private BigDecimal valorAmortizacao;

    @Column(name = "valor_juros", precision = 15, scale = 2)
    private BigDecimal valorJuros;

    @Column(name = "valor_parcela", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorParcela;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDateTime dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusParcela status = StatusParcela.PENDENTE;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Emprestimo emprestimo) {
		this.emprestimo = emprestimo;
	}

	public Integer getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(Integer numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public BigDecimal getValorAmortizacao() {
		return valorAmortizacao;
	}

	public void setValorAmortizacao(BigDecimal valorAmortizacao) {
		this.valorAmortizacao = valorAmortizacao;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public LocalDateTime getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDateTime dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDateTime getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDateTime dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public StatusParcela getStatus() {
		return status;
	}

	public void setStatus(StatusParcela status) {
		this.status = status;
	}
    
    
}
