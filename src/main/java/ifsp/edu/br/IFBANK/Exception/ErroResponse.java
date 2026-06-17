package ifsp.edu.br.IFBANK.Exception;

import java.time.LocalDateTime;

/**
 * Corpo padronizado retornado em todas as respostas de erro da API.
 *
 * Exemplo de payload:
 * {
 *   "timestamp": "2025-06-15T14:32:00",
 *   "status": 404,
 *   "erro": "Recurso não encontrado",
 *   "mensagem": "Conta não encontrada"
 * }
 */
public class ErroResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String erro;
    private final String mensagem;

    public ErroResponse(int status, String erro, String mensagem) {
        this.timestamp = LocalDateTime.now();
        this.status    = status;
        this.erro      = erro;
        this.mensagem  = mensagem;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int           getStatus()    { return status;    }
    public String        getErro()      { return erro;      }
    public String        getMensagem()  { return mensagem;  }
}
