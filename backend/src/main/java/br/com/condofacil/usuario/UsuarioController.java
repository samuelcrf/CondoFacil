package br.com.condofacil.usuario;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.condofacil.seguranca.UsuarioAutenticado;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final ConsultaDeUsuariosServico consultaDeUsuariosServico;

    public UsuarioController(ConsultaDeUsuariosServico consultaDeUsuariosServico) {
        this.consultaDeUsuariosServico = consultaDeUsuariosServico;
    }

    @GetMapping
    @PreAuthorize("hasRole('SINDICO')")
    public List<UsuarioResumoResposta> listar(@AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
        return consultaDeUsuariosServico.listarDoCondominio(usuarioLogado.getIdDoCondominio());
    }

    @GetMapping("/{idDoUsuario}")
    @PreAuthorize("hasRole('SINDICO')")
    public UsuarioResumoResposta buscarPorId(@PathVariable Long idDoUsuario,
                                             @AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
        return consultaDeUsuariosServico.buscarDoCondominio(idDoUsuario, usuarioLogado.getIdDoCondominio());
    }
}