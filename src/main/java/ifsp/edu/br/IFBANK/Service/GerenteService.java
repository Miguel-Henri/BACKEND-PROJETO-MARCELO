package ifsp.edu.br.IFBANK.Service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ifsp.edu.br.IFBANK.Repository.ContaRepository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.ContaResumoDTO;
import ifsp.edu.br.IFBANK.model.GerenteAcaoRequest;
import ifsp.edu.br.IFBANK.model.enums.StatusConta;
import ifsp.edu.br.IFBANK.model.enums.TipoConta;

@Service
public class GerenteService {

    private final ContaRepository contaRepository;

    public GerenteService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    /**
     * Valida se a conta informada realmente pertence a um gerente.
     * Lança exceção se não for gerente ou se a conta não existir.
     */
    private void validarGerente(Integer gerenteContaId) {
        Conta contaGerente = contaRepository.findById(gerenteContaId)
            .orElseThrow(() -> new NoSuchElementException("Conta do gerente não encontrada"));

        if (contaGerente.getRole() != TipoConta.GERENTE) {
            throw new SecurityException("Acesso negado: a conta informada não possui perfil de gerente");
        }

        if (contaGerente.getStatus() != StatusConta.ATIVA) {
            throw new SecurityException("Acesso negado: a conta do gerente está inativa ou bloqueada");
        }
    }

    /**
     * Lista todas as contas de clientes com status PENDENTE (aguardando aprovação).
     * Ordenadas pela data de criação mais antiga primeiro.
     */
    public Page<ContaResumoDTO> listarContasPendentes( Pageable pageable) {
        return contaRepository
            .findByStatusAndRole(StatusConta.PENDENTE, TipoConta.CLIENTE, pageable)
            .map(ContaResumoDTO::from);
    }

    /**
     * Lista todas as contas de clientes independente do status.
     * Útil para o gerente ter uma visão geral de todos os clientes.
     */
    public Page<ContaResumoDTO> listarTodasContas( Pageable pageable) {
        return contaRepository
            .findByRole(TipoConta.CLIENTE, pageable)
            .map(ContaResumoDTO::from);
    }

    /**
     * Processa a ação do gerente sobre uma conta pendente.
     * Ação "APROVAR" → status vira ATIVA.
     * Ação "REJEITAR" → status vira BLOQUEADA.
     */
    @Transactional
    public void processarConta(GerenteAcaoRequest request) {
        validarGerente(request.getGerenteContaId());

        Conta conta = contaRepository.findById(request.getContaId())
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada: id " + request.getContaId()));

        if (conta.getRole() == TipoConta.GERENTE) {
            throw new IllegalArgumentException("Não é possível alterar o status de uma conta gerente por esta operação");
        }

        String acao = request.getAcao();

        if (acao == null || acao.isBlank()) {
            throw new IllegalArgumentException("Ação não informada. Use 'APROVAR' ou 'REJEITAR'");
        }

        switch (acao.toUpperCase()) {
            case "APROVAR" -> {
                if (conta.getStatus() == StatusConta.ATIVA) {
                    throw new IllegalArgumentException("Conta já está ativa");
                }
                conta.setStatus(StatusConta.ATIVA);
            }
            case "REJEITAR" -> {
                if (conta.getStatus() == StatusConta.BLOQUEADA) {
                    throw new IllegalArgumentException("Conta já está bloqueada");
                }
                conta.setStatus(StatusConta.BLOQUEADA);
            }
            default -> throw new IllegalArgumentException("Ação inválida: '" + acao + "'. Use 'APROVAR' ou 'REJEITAR'");
        }

        contaRepository.save(conta);
    }

    /**
     * Busca os detalhes de uma conta específica (para o gerente visualizar antes de decidir).
     */
    public ContaResumoDTO buscarConta( Integer contaId) {
      
        Conta conta = contaRepository.findById(contaId)
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada: id " + contaId));
        return ContaResumoDTO.from(conta);
    }
}
