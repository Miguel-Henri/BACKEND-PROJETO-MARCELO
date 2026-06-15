package ifsp.edu.br.IFBANK.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    /** GET /api/gerente/contas/pendentes */
    @GetMapping("/contas/pendentes")
    public ResponseEntity<Page<ContaResumoDTO>> listarPendentes(
        @RequestHeader("X-Gerente-Conta-Id") Integer gerenteContaId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(gerenteService.listarContasPendentes(
            gerenteContaId, PageRequest.of(page, size, Sort.by("dataCriacao").ascending())));
    }

    /** GET /api/gerente/contas */
    @GetMapping("/contas")
    public ResponseEntity<Page<ContaResumoDTO>> listarTodas(
        @RequestHeader("X-Gerente-Conta-Id") Integer gerenteContaId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(gerenteService.listarTodasContas(
            gerenteContaId, PageRequest.of(page, size, Sort.by("dataCriacao").descending())));
    }

    /** GET /api/gerente/contas/{contaId} */
    @GetMapping("/contas/{contaId}")
    public ResponseEntity<ContaResumoDTO> buscarConta(
        @RequestHeader("X-Gerente-Conta-Id") Integer gerenteContaId,
        @PathVariable Integer contaId
    ) {
        return ResponseEntity.ok(gerenteService.buscarConta(gerenteContaId, contaId));
    }

    /** PATCH /api/gerente/contas/acao */
    @PatchMapping("/contas/acao")
    public ResponseEntity<String> processarConta(@RequestBody GerenteAcaoRequest request) {
        gerenteService.processarConta(request);
        String msg = "APROVAR".equalsIgnoreCase(request.getAcao())
            ? "Conta aprovada com sucesso"
            : "Conta rejeitada com sucesso";
        return ResponseEntity.ok(msg);
    }
}
