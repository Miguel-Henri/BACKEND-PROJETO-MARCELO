package ifsp.edu.br.IFBANK.Controller;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.GerenteService;
import ifsp.edu.br.IFBANK.model.ContaResumoDTO;
import ifsp.edu.br.IFBANK.model.GerenteAcaoRequest;

@RestController
@RequestMapping("/api/gerente")
public class GerenteController {

    private final GerenteService gerenteService;

    public GerenteController(GerenteService gerenteService) {
        this.gerenteService = gerenteService;
    }

    /**
     * Lista contas de clientes com status PENDENTE (aguardando aprovação).
     *
     * GET /api/gerente/contas/pendentes?page=0&size=10
     *
     * Header obrigatório: X-Gerente-Conta-Id → ID da conta do gerente logado.
     */
    @GetMapping("/contas/pendentes")
    public ResponseEntity<?> listarPendentes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<ContaResumoDTO> resultado = gerenteService.listarContasPendentes(
                PageRequest.of(page, size, Sort.by("dataCriacao").ascending())
            );
            return ResponseEntity.ok(resultado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    /**
     * Lista todas as contas de clientes (visão geral do gerente).
     *
     * GET /api/gerente/contas?page=0&size=10
     *
     * Header obrigatório: X-Gerente-Conta-Id → ID da conta do gerente logado.
     */
    @GetMapping("/contas")
    public ResponseEntity<?> listarTodas(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<ContaResumoDTO> resultado = gerenteService.listarTodasContas(
                PageRequest.of(page, size, Sort.by("dataCriacao").descending())
            );
            return ResponseEntity.ok(resultado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    /**
     * Busca os detalhes de uma conta específica.
     *
     * GET /api/gerente/contas/{contaId}
     *
     * Header obrigatório: X-Gerente-Conta-Id → ID da conta do gerente logado.
     */
    @GetMapping("/contas/{contaId}")
    public ResponseEntity<?> buscarConta(
        @PathVariable Integer contaId
    ) {
        try {
            ContaResumoDTO dto = gerenteService.buscarConta(contaId);
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    /**
     * Aprova ou rejeita a abertura de uma conta.
     *
     * PATCH /api/gerente/contas/acao
     *
     * Body JSON:
     * {
     *   "contaId": 5,
     *   "gerenteContaId": 1,
     *   "acao": "APROVAR"   ← ou "REJEITAR"
     * }
     */
    @PatchMapping("/contas/acao")
    public ResponseEntity<String> processarConta(@RequestBody GerenteAcaoRequest request) {
        try {
            gerenteService.processarConta(request);
            String msg = "APROVAR".equalsIgnoreCase(request.getAcao())
                ? "Conta aprovada com sucesso"
                : "Conta rejeitada com sucesso";
            return ResponseEntity.ok(msg);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
