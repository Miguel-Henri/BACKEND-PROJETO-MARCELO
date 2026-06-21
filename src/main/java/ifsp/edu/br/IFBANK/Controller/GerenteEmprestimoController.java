package ifsp.edu.br.IFBANK.Controller;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.br.IFBANK.Service.EmprestimoService;

@RestController
@RequestMapping("/api/gerente/emprestimos")
public class GerenteEmprestimoController {

    private final EmprestimoService emprestimoService;

    public GerenteEmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    // GET /api/gerente/emprestimos/pendentes
    @GetMapping("/pendentes")
    public ResponseEntity<?> listarPendentes() {
        return ResponseEntity.ok(emprestimoService.listarPendentes());
    }

    // POST /api/gerente/emprestimos/{id}/aprovar
    @PostMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(emprestimoService.aprovar(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // POST /api/gerente/emprestimos/{id}/rejeitar
    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<?> rejeitar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(emprestimoService.rejeitar(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
