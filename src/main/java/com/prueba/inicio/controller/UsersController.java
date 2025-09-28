package com.prueba.inicio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.inicio.model.User;
import com.prueba.inicio.service.UserService;
@RestController
@RequestMapping("/user")
public class UsersController {

	private final UserService service = new UserService();

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> list(
			@RequestParam(value = "sortedBy", required = false) String sortedBy,
			@RequestParam(value = "filter", required = false) String filter) {
		List<User> list = service.listAll(Optional.ofNullable(sortedBy), Optional.ofNullable(filter));
		List<Map<String, Object>> out = list.stream().map(u -> {
			Map<String, Object> m = new HashMap<>();
			m.put("id", u.getId());
			m.put("email", u.getEmail());
			m.put("name", u.getName());
			m.put("phone", u.getPhone());
			m.put("tax_id", u.getTax_id());
			m.put("created_at", u.getCreated_at());
			m.put("addresses", u.getAddresses());
			return m;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(out);
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
		// minimal mapping
		User u = mapToUser(body);
		User created = service.create(u);
		Map<String, Object> out = userToMap(created);
		return ResponseEntity.ok(out);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Map<String, Object>> patch(@PathVariable String id,
			@RequestBody Map<String, Object> updates) {
		User updated = service.patch(UUID.fromString(id), updates);
		return ResponseEntity.ok(userToMap(updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(UUID.fromString(id));
		return ResponseEntity.noContent().build();
	}

	private User mapToUser(Map<String, Object> b) {
		User u = new User();
		u.setEmail((String) b.get("email"));
		u.setName((String) b.get("name"));
		u.setPhone((String) b.get("phone"));
		// caller sends plaintext password in "password"
		u.setPasswordEncrypted((String) b.get("password"));
		u.setTax_id((String) b.get("tax_id"));
		// addresses mapping omitted for brevity
		return u;
	}

	private Map<String, Object> userToMap(User u) {
		Map<String, Object> m = new HashMap<>();
		m.put("id", u.getId());
		m.put("email", u.getEmail());
		m.put("name", u.getName());
		m.put("phone", u.getPhone());
		m.put("tax_id", u.getTax_id());
		m.put("created_at", u.getCreated_at());
		m.put("addresses", u.getAddresses());
		// password excluded per requirement
		return m;
	}

}
