package dev.luizveronesi.autoconfigure.security;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;

import dev.luizveronesi.autoconfigure.exception.ApplicationException;
import dev.luizveronesi.autoconfigure.utils.AuthUtil;
import dev.luizveronesi.autoconfigure.utils.JsonUtil;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	public final static String EXTRA_HEADER = "X-";
	public final static String JWT_HEADER = "Authorization";
	public final static String JWT_TOKEN_PREFIX = "Bearer";

	private final IUserService userService;

	public JwtAuthorizationFilter(IUserService userService) {
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(JWT_HEADER);
		if (!StringUtils.hasLength(token) || !token.startsWith(JWT_TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		var extra = this.getExtraHeaders(request);

		try {
			SecurityContextHolder.getContext().setAuthentication(getAuthentication(token, extra));
			chain.doFilter(request, response);
		} catch (RuntimeException e) {
			throw new ApplicationException(
					HttpStatus.BAD_REQUEST, 
					ExceptionUtils.getMessage(e));
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token, Map<String, String> extra) {
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String[] chunks = token.split("\\.");

		IUser user = JsonUtil.deserialize(
				new String(decoder.decode(chunks[1])),
				new TypeReference<IUser>() {
				});

		var optional = userService.findForAuthentication(user);
		if (optional == null)
			throw new AccessDeniedException("invalid credentials");

		return new UsernamePasswordAuthenticationToken(
				optional,
				token,
				AuthUtil.getAuthorities(optional));
	}

	private Map<String, String> getExtraHeaders(HttpServletRequest request) {
		var extra = new HashMap<String, String>();
		var headers = request.getHeaderNames().asIterator();
		while (headers.hasNext()) {
			var header = headers.next();
			if (header.startsWith(EXTRA_HEADER))
				extra.put(header.replace(
						EXTRA_HEADER, ""),
						request.getHeader(header));
		}
		return extra;
	}
}
