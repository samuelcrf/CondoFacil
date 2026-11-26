package br.com.condofacil.seguranca;

import java.io.IOException;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FiltroDeCookieCsrf extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest requisicao, HttpServletResponse resposta,
			FilterChain cadeiaDeFiltros) throws ServletException, IOException {
		CsrfToken tokenCsrf = (CsrfToken) requisicao.getAttribute("_csrf");
		if (tokenCsrf != null) {
			// Ler o valor do token faz o Spring gravar
			tokenCsrf.getToken();
		}
		cadeiaDeFiltros.doFilter(requisicao, resposta);
	}
}
