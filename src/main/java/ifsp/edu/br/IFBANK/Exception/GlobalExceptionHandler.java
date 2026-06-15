package ifsp.edu.br.IFBANK.Exception;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Tratamento global de exceções da API IFBank.
 *
 * Centraliza todos os casos de erro, eliminando try/catch repetidos
 * nos controllers e garantindo respostas JSON padronizadas com:
 *   timestamp · status HTTP · descrição do erro · mensagem detalhada
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----------------------------------------------------------------
    // 404 – recurso inexistente
    // ----------------------------------------------------------------
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErroResponse> handleNotFound(NoSuchElementException ex) {
        return build(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    // ----------------------------------------------------------------
    // 400 – regra de negócio violada (valor negativo, saldo insuficiente …)
    // ----------------------------------------------------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleBadRequest(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage());
    }

    // ----------------------------------------------------------------
    // 403 – acesso negado (gerente inválido, conta bloqueada …)
    // ----------------------------------------------------------------
    @ExceptionHandler({ SecurityException.class, IllegalStateException.class })
    public ResponseEntity<ErroResponse> handleForbidden(RuntimeException ex) {
        return build(HttpStatus.FORBIDDEN, "Acesso negado", ex.getMessage());
    }

    // ----------------------------------------------------------------
    // 401 – credenciais inválidas
    //       (lançada como IllegalArgumentException com mensagem específica
    //        no UsuarioService; mantemos handler dedicado via subtipo)
    // ----------------------------------------------------------------
    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroResponse> handleUnauthorized(CredenciaisInvalidasException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Não autorizado", ex.getMessage());
    }

    // ----------------------------------------------------------------
    // 409 – conflito de integridade (e-mail duplicado …)
    // ----------------------------------------------------------------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleConflict(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Conflito de dados", "Registro já existe ou viola restrição de integridade.");
    }

    // ----------------------------------------------------------------
    // 400 – corpo JSON malformado
    // ----------------------------------------------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido", "JSON malformado ou tipo de dado incorreto.");
    }

    // ----------------------------------------------------------------
    // 400 – parâmetro de query obrigatório ausente
    // ----------------------------------------------------------------
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Parâmetro obrigatório ausente: " + ex.getParameterName();
        return build(HttpStatus.BAD_REQUEST, "Parâmetro ausente", msg);
    }

    // ----------------------------------------------------------------
    // 400 – header obrigatório ausente (ex.: X-Gerente-Conta-Id)
    // ----------------------------------------------------------------
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErroResponse> handleMissingHeader(MissingRequestHeaderException ex) {
        String msg = "Header obrigatório ausente: " + ex.getHeaderName();
        return build(HttpStatus.BAD_REQUEST, "Header ausente", msg);
    }

    // ----------------------------------------------------------------
    // 400 – tipo de parâmetro incompatível (ex.: letra onde esperava número)
    // ----------------------------------------------------------------
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Parâmetro '" + ex.getName() + "' com tipo inválido.";
        return build(HttpStatus.BAD_REQUEST, "Tipo de parâmetro inválido", msg);
    }

    // ----------------------------------------------------------------
    // 500 – qualquer outra exceção não mapeada
    // ----------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenerico(Exception ex) {
        // Loga no servidor mas não expõe detalhes internos ao cliente
        ex.printStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    // ----------------------------------------------------------------
    // helper
    // ----------------------------------------------------------------
    private ResponseEntity<ErroResponse> build(HttpStatus status, String erro, String mensagem) {
        return ResponseEntity
                .status(status)
                .body(new ErroResponse(status.value(), erro, mensagem));
    }
}
