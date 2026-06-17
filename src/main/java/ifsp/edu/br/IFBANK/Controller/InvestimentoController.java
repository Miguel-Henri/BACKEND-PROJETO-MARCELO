package ifsp.edu.br.IFBANK.Controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.InvestimentoService;
import ifsp.edu.br.IFBANK.model.Investimento;
import ifsp.edu.br.IFBANK.model.InvestimentoRequest;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    // POST /api/investimentos/aplicar
    @PostMapping("/aplicar")
    public ResponseEntity<?> aplicar(@RequestBody InvestimentoRequest request) {
        try {
            Investimento investimento = investimentoService.aplicar(request);
            return ResponseEntity.ok(investimento);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // GET /api/investimentos/listar?numeroConta=10001&agencia=5
    @GetMapping("/listar")
    public ResponseEntity<?> listar(
            @RequestParam Integer numeroConta,
            @RequestParam Integer agencia) {
        try {
            List<Investimento> investimentos = investimentoService.listar(numeroConta, agencia);
            return ResponseEntity.ok(investimentos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/investimentos/{id}/encerrar?numeroConta=10001&agencia=5
    @PostMapping("/{id}/encerrar")
    public ResponseEntity<?> encerrar(
            @PathVariable Integer id,
            @RequestParam Integer numeroConta,
            @RequestParam Integer agencia) {
        try {
            Investimento investimento = investimentoService.encerrar(id, numeroConta, agencia);
            return ResponseEntity.ok(investimento);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
