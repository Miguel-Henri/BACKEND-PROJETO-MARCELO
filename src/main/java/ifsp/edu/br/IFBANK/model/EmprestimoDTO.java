package ifsp.edu.br.IFBANK.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ifsp.edu.br.IFBANK.model.enums.StatusEmprestimo;

public class EmprestimoDTO {

    private Integer id;
    private BigDecimal valorEmprestimo;
    private BigDecimal taxaJurosMensal;
    private Integer parcelas;
    private StatusEmprestimo status;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataAprovacao;
    private LocalDateTime dataUltimoPagamento;
    private Integer numeroConta;
    private List<ParcelaDTO> parcelasEmprestimo;

    public static EmprestimoDTO from(Emprestimo e) {
        EmprestimoDTO dto = new EmprestimoDTO();
        dto.id = e.getId();
        dto.valorEmprestimo = e.getValorEmprestimo();
        dto.taxaJurosMensal = e.getTaxaJurosMensal();
        dto.parcelas = e.getParcelas();
        dto.status = e.getStatus();
        dto.dataSolicitacao = e.getDataSolicitacao();
        dto.dataAprovacao = e.getDataAprovacao();
        dto.dataUltimoPagamento = e.getDataUltimoPagamento();
        dto.numeroConta = e.getConta() != null ? e.getConta().getNumeroConta() : null;

        List<ParcelaDTO> parcelasDTO = new ArrayList<>();
        if (e.getParcelasEmprestimo() != null) {
            for (ParcelaEmprestimo p : e.getParcelasEmprestimo()) {
                parcelasDTO.add(ParcelaDTO.from(p));
            }
        }
        dto.parcelasEmprestimo = parcelasDTO;
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getValorEmprestimo() {
        return valorEmprestimo;
    }

    public BigDecimal getTaxaJurosMensal() {
        return taxaJurosMensal;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public LocalDateTime getDataUltimoPagamento() {
        return dataUltimoPagamento;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public List<ParcelaDTO> getParcelasEmprestimo() {
        return parcelasEmprestimo;
    }
}
