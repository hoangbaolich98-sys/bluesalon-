package com.bluesalon.service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenService {
	private final Set<String> validTokens = ConcurrentHashMap.newKeySet();

	public String taoToken() {
		String token = UUID.randomUUID().toString();
		validTokens.add(token);
		return token;
	}

	public boolean kiemTra(String token) {
		return token != null && validTokens.contains(token);
	}
}
