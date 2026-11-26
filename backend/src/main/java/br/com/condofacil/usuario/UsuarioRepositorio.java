package br.com.condofacil.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//repositorio de usuarios
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

	// busca por email
	Optional<Usuario> findByEmailIgnoreCase(String email);

	// informa se ja existe usuario com o email informado
	boolean existsByEmailIgnoreCase(String email);

	// lista usuarios de um condominio e com perfil especifico
	List<Usuario> findAllByCondominioIdAndPerfil(Long condominioId, PerfilDeUsuario perfil);

	List<Usuario> findAllByCondominioIdOrderByNomeAsc(Long condominioId);

	// busca o id mas somente se pertencer a determinado condominio
	// garante o isolamento us-8
	Optional<Usuario> findByIdAndCondominioId(Long id, Long condominioId);
}