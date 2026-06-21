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

import ifsp.edu.br.IFBANK.Service.EmprestimoService;
import ifsp.edu.br.IFBANK.model.EmprestimoDTO;
import ifsp.edu.br.IFBANK.model.EmprestimoRequest;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    // POST /api/emprestimos/solicitar
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitar(@RequestBody EmprestimoRequest request) {
        try {
            EmprestimoDTO emprestimo = emprestimoService.solicitar(request);
            return ResponseEntity.ok(emprestimo);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // GET /api/emprestimos/listar?numeroConta=10001&agencia=5
    @GetMapping("/listar")
    public ResponseEntity<?> listar(
            @RequestParam Integer numeroConta,
            @RequestParam Integer agencia) {
        try {
            List<EmprestimoDTO> emprestimos = emprestimoService.listar(numeroConta, agencia);
            return ResponseEntity.ok(emprestimos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/emprestimos/{id}/parcelas/{parcelaId}/pagar?numeroConta=10001&agencia=5
    @PostMapping("/{id}/parcelas/{parcelaId}/pagar")
    public ResponseEntity<?> pagarParcela(
            @PathVariable Integer id,
            @PathVariable Integer parcelaId,
            @RequestParam Integer numeroConta,
            @RequestParam Integer agencia) {
        try {
            EmprestimoDTO emprestimo = emprestimoService.pagarParcela(id, parcelaId, numeroConta, agencia);
            return ResponseEntity.ok(emprestimo);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
