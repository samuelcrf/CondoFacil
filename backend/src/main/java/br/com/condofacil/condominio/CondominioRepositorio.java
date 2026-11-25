package br.com.condofacil.condominio;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CondominioRepositorio extends JpaRepository<Condominio, Long> {
}