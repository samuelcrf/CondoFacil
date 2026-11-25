package br.com.condofacil.chamado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepositorio extends JpaRepository<Chamado, Long> {

	List<Chamado> findAllByCondominioId(Long condominioId);
	List<Chamado> findAllByMoradorId(Long moradorId);
}
