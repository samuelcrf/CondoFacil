package br.com.condofacil.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCase(String email);

	List<Usuario> findAllByCondominioIdAndPerfil(Long condominioId, PerfilDeUsuario perfil);
}