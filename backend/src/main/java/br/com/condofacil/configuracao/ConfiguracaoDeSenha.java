package br.com.condofacil.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ConfiguracaoDeSenha {

	@Bean
	public PasswordEncoder codificadorDeSenha() {
		return new BCryptPasswordEncoder();
	}
}