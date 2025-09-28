package com.prueba.inicio.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.inicio.service.UserService;
@RestController
public class LoginController {
	
	private final UserService service = new UserService();


	@PostMapping("/login")
	public ResponseEntity<Map<String,Object>> login(@RequestBody Map<String,String> body){
	String taxId = body.get("tax_id");
	String password = body.get("password");
	if (taxId==null || password==null) return ResponseEntity.badRequest().body(Map.of("error","tax_id and password required"));
	boolean ok = service.authenticate(taxId, password);
	if (!ok) return ResponseEntity.status(401).body(Map.of("error","invalid_credentials"));
	return ResponseEntity.ok(Map.of("status","ok"));
	}

}
