package ifsp.edu.br.IFBANK.Service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ifsp.edu.br.IFBANK.Repository.ContaRepository;
import ifsp.edu.br.IFBANK.Repository.UsuarioRepository;
import ifsp.edu.br.IFBANK.model.Conta;
import ifsp.edu.br.IFBANK.model.ContaResumoDTO;
import ifsp.edu.br.IFBANK.model.GerenteAcaoRequest;
import ifsp.edu.br.IFBANK.model.Usuario;
import ifsp.edu.br.IFBANK.model.enums.StatusConta;
import ifsp.edu.br.IFBANK.model.enums.StatusUsuario;
import ifsp.edu.br.IFBANK.model.enums.TipoConta;

@Service
public class GerenteService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    public GerenteService(ContaRepository contaRepository, UsuarioRepository usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
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


    @Transactional
    public void processarConta(GerenteAcaoRequest request) {
        Conta conta = contaRepository.findById(request.getContaId())
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada: id " + request.getContaId()));

            Usuario usuario = usuarioRepository.findById(request.getContaId())
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado: id " + request.getContaId()));

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
                usuario.setStatus(StatusUsuario.ATIVO);
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


    public ContaResumoDTO buscarConta( Integer contaId) {
      
        Conta conta = contaRepository.findById(contaId)
            .orElseThrow(() -> new NoSuchElementException("Conta não encontrada: id " + contaId));
        return ContaResumoDTO.from(conta);
    }
}