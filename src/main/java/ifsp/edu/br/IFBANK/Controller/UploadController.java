package ifsp.edu.br.IFBANK.Controller;

import ifsp.edu.br.IFBANK.Service.UsuarioService;
import ifsp.edu.br.IFBANK.model.Usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {
	
	private final UsuarioService usuarioService;

	public UploadController(UsuarioService usuarioService) {
	    this.usuarioService = usuarioService;
	}

	@PostMapping("/{id}/foto")
    public ResponseEntity<String> upload( @PathVariable Integer id,@RequestParam("file") MultipartFile file) {

        try {

            // cria pasta uploads
            Path pastaUpload = Paths.get("uploads");

            if (!Files.exists(pastaUpload)) {
                Files.createDirectories(pastaUpload);
            }

            // gera nome único
            String nomeArquivo = UUID.randomUUID()
                    + "_"
                    + file.getOriginalFilename();

            // caminho final
            Path caminhoArquivo =
                    pastaUpload.resolve(nomeArquivo);

            // salva arquivo
            Files.copy(
                    file.getInputStream(),
                    caminhoArquivo,
                    StandardCopyOption.REPLACE_EXISTING
            );
            
            Usuario usuario = usuarioService.buscarPorId(id);

            usuario.setFotoPerfil(nomeArquivo);

            usuarioService.salvar(usuario);

            return ResponseEntity.ok(
                    "Imagem salva: " + nomeArquivo
            );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Erro ao salvar imagem");
        }
    }
}