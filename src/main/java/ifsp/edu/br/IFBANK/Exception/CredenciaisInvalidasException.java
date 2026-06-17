package ifsp.edu.br.IFBANK.Exception;

/**
 * Lançada quando e-mail ou senha não conferem no login.
 * Mapeada para HTTP 401 pelo GlobalExceptionHandler.
 */
public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
