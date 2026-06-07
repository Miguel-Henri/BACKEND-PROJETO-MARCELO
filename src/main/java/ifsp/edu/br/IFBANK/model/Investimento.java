package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ifsp.edu.br.IFBANK.model.enums.StatusInvestimento;
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
@Table(name = "investimentos")
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Column(name = "tipo_investimento", nullable = false, length = 100)
    private String tipoInvestimento;

    @Column(name = "valor_investido", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorInvestido;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio = LocalDateTime.now();

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusInvestimento status = StatusInvestimento.ATIVO;

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

	public String getTipoInvestimento() {
		return tipoInvestimento;
	}

	public void setTipoInvestimento(String tipoInvestimento) {
		this.tipoInvestimento = tipoInvestimento;
	}

	public BigDecimal getValorInvestido() {
		return valorInvestido;
	}

	public void setValorInvestido(BigDecimal valorInvestido) {
		this.valorInvestido = valorInvestido;
	}

	public LocalDateTime getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDateTime getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDateTime dataFim) {
		this.dataFim = dataFim;
	}

	public StatusInvestimento getStatus() {
		return status;
	}

	public void setStatus(StatusInvestimento status) {
		this.status = status;
	}
    
    
}

