package com.bluesalon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bluesalon.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getParameter("token");

		if (!tokenService.kiemTra(token)) {
			response.setStatus(401);
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().write("Loi: Chua dang nhap hoac phien da het han!");
			return false;
		}
		return true;
	}
}
