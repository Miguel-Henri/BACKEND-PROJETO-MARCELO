package ifsp.edu.br.IFBANK.Controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.ContaService;
import ifsp.edu.br.IFBANK.model.DepositoRequest;
import ifsp.edu.br.IFBANK.model.MovimentacaoDTO;
import ifsp.edu.br.IFBANK.model.TransferenciaRequest;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> transferir(@RequestBody TransferenciaRequest request) {
        contaService.transferir(request);
        return ResponseEntity.ok("Transferência realizada com sucesso");
    }

    @PostMapping("/deposito")
    public ResponseEntity<String> depositar(@RequestBody DepositoRequest request) {
        contaService.depositar(request);
        return ResponseEntity.ok("Depósito realizado com sucesso");
    }

    @GetMapping("/extrato")
    public ResponseEntity<Page<MovimentacaoDTO>> extrato(
        @RequestParam Integer numeroConta,
        @RequestParam Integer agencia,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<MovimentacaoDTO> resultado = contaService.extrato(
            numeroConta, agencia, dataInicio, dataFim, PageRequest.of(page, size));
        return ResponseEntity.ok(resultado);
    }
}
