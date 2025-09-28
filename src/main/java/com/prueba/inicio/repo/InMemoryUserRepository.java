package com.prueba.inicio.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.prueba.inicio.model.User;
@Repository
public class InMemoryUserRepository {
	private final List<User> users = new CopyOnWriteArrayList<>();


	public List<User> findAll() { return new ArrayList<>(users); }
	public Optional<User> findById(UUID id) { return users.stream().filter(u->u.getId().equals(id)).findFirst(); }
	public Optional<User> findByTaxId(String taxId) { return users.stream().filter(u->u.getTax_id().equals(taxId)).findFirst(); }
	public void save(User u) { users.add(u); }
	public void delete(UUID id) { users.removeIf(u->u.getId().equals(id)); }
	public boolean existsByTaxId(String taxId) { return users.stream().anyMatch(u->u.getTax_id().equals(taxId)); }


	public void seed(List<User> initial) { users.addAll(initial); }
}
