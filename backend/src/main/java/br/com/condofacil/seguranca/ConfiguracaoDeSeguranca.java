package br.com.condofacil.seguranca;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.condofacil.erro.RespostaDeErro;
import jakarta.servlet.http.HttpServletResponse;

//Liga as anotações de permissão nos métodos dos controladores
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracaoDeSeguranca {

	private static final String MENSAGEM_DE_LOGIN_INVALIDO = "E-mail ou senha incorretos.";
	private static final String MENSAGEM_DE_ACESSO_NEGADO = "Você não tem permissão para realizar esta ação.";

	@Bean
	public SecurityFilterChain cadeiaDeFiltrosDeSeguranca(HttpSecurity http, ObjectMapper conversorDeJson)
			throws Exception {

		http
				//csrf
				.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
						.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
				.addFilterAfter(new FiltroDeCookieCsrf(), BasicAuthenticationFilter.class)

				//qual autor pode acessar o que
				.authorizeHttpRequests(regras -> regras
						// Endereços públicos (não exigem login)
						.requestMatchers(HttpMethod.GET, "/api/saude").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/condominios").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/autenticacao/login", "/api/autenticacao/logout")
						.permitAll()

						.requestMatchers("/api/**").authenticated()

						.anyRequest().permitAll())


				.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/api/autenticacao/login")
						.usernameParameter("email").passwordParameter("senha")
						.successHandler((requisicao, resposta, autenticacao) -> {

							CsrfToken tokenCsrf = (CsrfToken) requisicao.getAttribute("_csrf");
							if (tokenCsrf != null) {
								tokenCsrf.getToken();
							}
							escreverJson(conversorDeJson, resposta, HttpStatus.OK, UsuarioLogadoResposta
									.deUsuarioAutenticado((UsuarioAutenticado) autenticacao.getPrincipal()));
						})

						.failureHandler((requisicao, resposta, excecao) -> escreverJson(conversorDeJson, resposta,
								HttpStatus.UNAUTHORIZED, new RespostaDeErro(MENSAGEM_DE_LOGIN_INVALIDO, Map.of())))
						.permitAll())

				//logout
				// Encerra a sessão no servidor e apaga o cookie
				.logout(logout -> logout.logoutUrl("/api/autenticacao/logout").invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.logoutSuccessHandler((requisicao, resposta, autenticacao) -> resposta
								.setStatus(HttpStatus.NO_CONTENT.value()))
						.permitAll())

				.exceptionHandling(
						erros -> erros.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
								.accessDeniedHandler((requisicao, resposta, excecao) -> escreverJson(conversorDeJson,
										resposta, HttpStatus.FORBIDDEN,
										new RespostaDeErro(MENSAGEM_DE_ACESSO_NEGADO, Map.of()))));

		return http.build();
	}

	private static void escreverJson(ObjectMapper conversorDeJson, HttpServletResponse resposta, HttpStatus status,
			Object corpo) throws IOException {
		resposta.setStatus(status.value());
		resposta.setContentType(MediaType.APPLICATION_JSON_VALUE);
		resposta.setCharacterEncoding("UTF-8");
		conversorDeJson.writeValue(resposta.getWriter(), corpo);
	}
}