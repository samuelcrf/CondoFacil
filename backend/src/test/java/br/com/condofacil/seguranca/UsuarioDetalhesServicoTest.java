package br.com.condofacil.seguranca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.condofacil.condominio.Condominio;
import br.com.condofacil.usuario.PerfilDeUsuario;
import br.com.condofacil.usuario.Usuario;
import br.com.condofacil.usuario.UsuarioRepositorio;


 //Teste unitário do serviço que busca o usuário para o login 
 
@ExtendWith(MockitoExtension.class)
class UsuarioDetalhesServicoTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @InjectMocks
    private UsuarioDetalhesServico servico;

    private Usuario criarUsuario(PerfilDeUsuario perfil) {
        Condominio condominio = new Condominio("Condomínio A", "Rua ABC, 100");
        return new Usuario(condominio, "Usuário A", "usuario@condominio-a.com", "hash-da-senha", perfil);
    }

    @Test
    void deveCarregarOUsuarioComEmailSenhaEPermissaoDoPerfil() {
        when(usuarioRepositorio.findByEmailIgnoreCase("usuario@condominio-a.com"))
                .thenReturn(Optional.of(criarUsuario(PerfilDeUsuario.SINDICO)));

        UserDetails usuarioCarregado = servico.loadUserByUsername("usuario@condominio-a.com");

        assertEquals("usuario@condominio-a.com", usuarioCarregado.getUsername());
        assertEquals("hash-da-senha", usuarioCarregado.getPassword());
        assertEquals("ROLE_SINDICO", usuarioCarregado.getAuthorities().iterator().next().getAuthority());
        assertTrue(usuarioCarregado.isEnabled());
    }

    @Test
    void deveIgnorarEspacosAoRedorDoEmailDigitado() {
        when(usuarioRepositorio.findByEmailIgnoreCase("usuario@condominio-a.com"))
                .thenReturn(Optional.of(criarUsuario(PerfilDeUsuario.MORADOR)));

        UserDetails usuarioCarregado = servico.loadUserByUsername("  usuario@condominio-a.com ");

        assertEquals("ROLE_MORADOR", usuarioCarregado.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void deveMarcarComoDesabilitadoUmUsuarioInativo() {
        Usuario usuarioInativo = criarUsuario(PerfilDeUsuario.PRESTADOR);
        usuarioInativo.setAtivo(false);
        when(usuarioRepositorio.findByEmailIgnoreCase("usuario@condominio-a.com"))
                .thenReturn(Optional.of(usuarioInativo));

        UserDetails usuarioCarregado = servico.loadUserByUsername("usuario@condominio-a.com");

        assertFalse(usuarioCarregado.isEnabled());
    }

    @Test
    void deveLancarErroQuandoOUsuarioNaoExiste() {
        when(usuarioRepositorio.findByEmailIgnoreCase("ninguem@condominio-a.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> servico.loadUserByUsername("ninguem@condominio-a.com"));
    }
}