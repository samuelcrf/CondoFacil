package br.com.condofacil.condominio;

import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.condofacil.usuario.EmailJaCadastradoExcecao;
import br.com.condofacil.usuario.PerfilDeUsuario;
import br.com.condofacil.usuario.Usuario;
import br.com.condofacil.usuario.UsuarioRepositorio;

@Service
public class CadastroDeCondominioServico {

	private final CondominioRepositorio condominioRepositorio;
	private final UsuarioRepositorio usuarioRepositorio;
	private final PasswordEncoder codificadorDeSenha;

	public CadastroDeCondominioServico(CondominioRepositorio condominioRepositorio,
			UsuarioRepositorio usuarioRepositorio, PasswordEncoder codificadorDeSenha) {
		this.condominioRepositorio = condominioRepositorio;
		this.usuarioRepositorio = usuarioRepositorio;
		this.codificadorDeSenha = codificadorDeSenha;
	}

	@Transactional
	public CondominioCadastradoResposta cadastrar(CadastroDeCondominioRequisicao requisicao) {
		// E-mail sempre guardado sem espaços e em minúsculas
		String emailNormalizado = requisicao.emailDoSindico().trim().toLowerCase(Locale.ROOT);

		if (usuarioRepositorio.existsByEmailIgnoreCase(emailNormalizado)) {
			throw new EmailJaCadastradoExcecao(emailNormalizado);
		}

		Condominio condominio = condominioRepositorio
				.save(new Condominio(requisicao.nomeDoCondominio().trim(), requisicao.enderecoDoCondominio().trim()));

		Usuario sindico = new Usuario(condominio, requisicao.nomeDoSindico().trim(), emailNormalizado,
				codificadorDeSenha.encode(requisicao.senhaDoSindico()), PerfilDeUsuario.SINDICO);

		String telefone = requisicao.telefoneDoSindico();
		if (telefone != null && !telefone.isBlank()) {
			sindico.setTelefone(telefone.trim());
		}

		try {
			sindico = usuarioRepositorio.saveAndFlush(sindico);
		} catch (DataIntegrityViolationException excecao) {

			throw new EmailJaCadastradoExcecao(emailNormalizado);
		}

		return new CondominioCadastradoResposta(condominio.getId(), condominio.getNome(), sindico.getId(),
				sindico.getEmail());
	}
}