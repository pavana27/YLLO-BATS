package org.temple.cis.kantlab.edgecomputing.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.temple.cis.kantlab.edgecomputing.server.resources.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Value("${ec.username}")
	private String userName;

	@Value("${ec.password}")
	private String password;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String loginId = request.getHeader("userName");
		String loginPassword = request.getHeader("password");

		if (loginId == null || !loginId.equalsIgnoreCase(userName) || loginPassword == null
				|| !loginPassword.equals(password)) {

			handleAuthenticationFailure(response);
			
			return false;
		}

		return true;
	}

	private void handleAuthenticationFailure(HttpServletResponse response) throws Exception {

		String errorResponse = new ObjectMapper().writeValueAsString(new Response("Authentication Failure"));
		response.setStatus(401);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.addHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
		try {
			response.getWriter().write(errorResponse);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
		}
	}
}