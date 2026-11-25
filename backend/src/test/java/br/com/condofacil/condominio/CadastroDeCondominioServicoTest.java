package br.com.condofacil.condominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.condofacil.usuario.EmailJaCadastradoExcecao;
import br.com.condofacil.usuario.PerfilDeUsuario;
import br.com.condofacil.usuario.Usuario;
import br.com.condofacil.usuario.UsuarioRepositorio;

@ExtendWith(MockitoExtension.class)
class CadastroDeCondominioServicoTest {

	@Mock
	private CondominioRepositorio condominioRepositorio;

	@Mock
	private UsuarioRepositorio usuarioRepositorio;

	@Mock
	private PasswordEncoder codificadorDeSenha;

	@InjectMocks
	private CadastroDeCondominioServico servico;

	private CadastroDeCondominioRequisicao criarRequisicao(String email) {

		return new CadastroDeCondominioRequisicao("  Condomínio A  ", "Rua ABC, 100", "Síndico A", email,
				"senha-segura-123", "(00) 00000-0000");
	}

	@Test
	void deveCadastrarCondominioESindicoComEmailNormalizadoESenhaCriptografada() {
		when(usuarioRepositorio.existsByEmailIgnoreCase("sindico@condominio-a.com")).thenReturn(false);
		when(codificadorDeSenha.encode("senha-segura-123")).thenReturn("hash-da-senha");

		when(condominioRepositorio.save(any(Condominio.class))).thenAnswer(chamada -> chamada.getArgument(0));
		when(usuarioRepositorio.saveAndFlush(any(Usuario.class))).thenAnswer(chamada -> chamada.getArgument(0));

		CondominioCadastradoResposta resposta = servico.cadastrar(criarRequisicao("  Sindico@Condominio-A.com "));

		ArgumentCaptor<Usuario> sindicoGravado = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepositorio).saveAndFlush(sindicoGravado.capture());
		assertEquals("sindico@condominio-a.com", sindicoGravado.getValue().getEmail());
		assertEquals("hash-da-senha", sindicoGravado.getValue().getSenhaCriptografada());
		assertEquals(PerfilDeUsuario.SINDICO, sindicoGravado.getValue().getPerfil());
		assertEquals("(00) 00000-0000", sindicoGravado.getValue().getTelefone());
		assertEquals("Condomínio A", resposta.nomeDoCondominio());
		assertEquals("sindico@condominio-a.com", resposta.emailDoSindico());
	}

	@Test
	void naoDeveCadastrarQuandoOEmailJaExiste() {
		when(usuarioRepositorio.existsByEmailIgnoreCase("sindico@condominio-a.com")).thenReturn(true);

		assertThrows(EmailJaCadastradoExcecao.class,
				() -> servico.cadastrar(criarRequisicao("sindico@condominio-a.com")));

		verify(condominioRepositorio, never()).save(any());
		verify(usuarioRepositorio, never()).saveAndFlush(any());
	}
}