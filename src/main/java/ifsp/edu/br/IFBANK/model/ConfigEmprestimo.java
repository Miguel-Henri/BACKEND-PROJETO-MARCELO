package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "config_emprestimo")
public class ConfigEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "taxa_juros_padrao", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJurosPadrao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getTaxaJurosPadrao() {
		return taxaJurosPadrao;
	}

	public void setTaxaJurosPadrao(BigDecimal taxaJurosPadrao) {
		this.taxaJurosPadrao = taxaJurosPadrao;
	}
    
    
}
