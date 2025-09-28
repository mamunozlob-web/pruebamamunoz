package com.prueba.inicio.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import java.util.Collections;

import com.prueba.inicio.model.Address;
import com.prueba.inicio.model.User;
import com.prueba.inicio.repo.InMemoryUserRepository;
import com.prueba.inicio.util.AESUtil;
import com.prueba.inicio.util.ValidationUtil;
@Service
public class UserService {

	private final InMemoryUserRepository repo = new InMemoryUserRepository();
	private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	private final ZoneId MADAGASCAR = ZoneId.of("Indian/Antananarivo");

	public UserService() {
		// seed 3 users as requested
		List<Address> addrs1 = new ArrayList<>();
		Address a1 = new Address();
		a1.setId(1);
		a1.setName("dirección del trabajo");
		a1.setCalle("calle n.° 1");
		a1.setCodigo_pais("RU");
		Address a2 = new Address();
		a2.setId(2);
		a2.setName("dirección_domicilio");
		a2.setCalle("calle n.° 2");
		a2.setCodigo_pais("AU");
		addrs1.add(a1);
		addrs1.add(a2);

		// user1
		User u1 = new User();
		u1.setId(UUID.randomUUID());
		u1.setEmail("user1@mail.com");
		u1.setName("user1");
		u1.setPhone("+1 55 555 555 55");
		u1.setPasswordEncrypted(AESUtil.encrypt("password1"));
		u1.setTax_id("AARR990101XXX");
		u1.setCreated_at(nowMadagascar());
		u1.setAddresses(addrs1);

		// user2
		User u2 = new User();
		u2.setId(UUID.randomUUID());
		u2.setEmail("alice@mail.com");
		u2.setName("alice");
		u2.setPhone("+34 612345678");
		u2.setPasswordEncrypted(AESUtil.encrypt("alicepass"));
		u2.setTax_id("ABCD8502021A2");
		u2.setCreated_at(nowMadagascar());
		u2.setAddresses(Collections.emptyList());

		// user3
		User u3 = new User();
		u3.setId(UUID.randomUUID());
		u3.setEmail("bob@mail.com");
		u3.setName("bob");
		u3.setPhone("5559876543");
		u3.setPasswordEncrypted(AESUtil.encrypt("bobpass"));
		u3.setTax_id("BCDE900303XXX");
		u3.setCreated_at(nowMadagascar());
		u3.setAddresses(Collections.emptyList());

		repo.seed(Arrays.asList(u1, u2, u3));
	}

	private String nowMadagascar() {
		return ZonedDateTime.now(MADAGASCAR).format(fmt);
	}

	public List<User> listAll(Optional<String> sortedBy, Optional<String> filter) {
		List<User> all = repo.findAll();
		// filter
		if (filter.isPresent()) {
			all = applyFilter(all, filter.get());
		}
		// sort
		if (sortedBy.isPresent()) {
			all = applySort(all, sortedBy.get());
		}
		return all;
	}

	private List<User> applyFilter(List<User> list, String filter) {
		// expected: attr+op+value
		String[] parts = filter.split("\\+", 3);
		if (parts.length != 3)
			return list;
		String attr = parts[0];
		String op = parts[1];
		String value = parts[2];
		return list.stream().filter(u -> match(u, attr, op, value)).collect(Collectors.toList());
	}

	private boolean match(User u, String attr, String op, String value) {
		String field = getAttr(u, attr);
		if (field == null)
			return false;
		field = field.toLowerCase();
		value = value.toLowerCase();
		switch (op) {
		case "co":
			return field.contains(value);
		case "eq":
			return field.equals(value);
		case "sw":
			return field.startsWith(value);
		case "ew":
			return field.endsWith(value);
		default:
			return false;
		}
	}

	private String getAttr(User u, String attr) {
		switch (attr) {
		case "email":
			return u.getEmail();
		case "id":
			return u.getId().toString();
		case "name":
			return u.getName();
		case "phone":
			return u.getPhone();
		case "tax_id":
			return u.getTax_id();
		case "created_at":
			return u.getCreated_at();
		default:
			return null;
		}
	}

	private List<User> applySort(List<User> list, String sortedBy) {
		Comparator<User> cmp;
		switch (sortedBy) {
		case "email":
			cmp = Comparator.comparing(User::getEmail, Comparator.nullsLast(String::compareToIgnoreCase));
			break;
		case "id":
			cmp = Comparator.comparing(User::getId);
			break;
		case "name":
			cmp = Comparator.comparing(User::getName, Comparator.nullsLast(String::compareToIgnoreCase));
			break;
		case "phone":
			cmp = Comparator.comparing(User::getPhone, Comparator.nullsLast(String::compareToIgnoreCase));
			break;
		case "tax_id":
			cmp = Comparator.comparing(User::getTax_id, Comparator.nullsLast(String::compareToIgnoreCase));
			break;
		case "created_at":
			cmp = Comparator.comparing(User::getCreated_at, Comparator.nullsLast(String::compareTo));
			break;
		default:
			return list;
		}
		return list.stream().sorted(cmp).collect(Collectors.toList());
	}

	public Optional<User> findById(UUID id) {
		return repo.findById(id);
	}

	public User create(User u) {
		// validations
		if (!ValidationUtil.validTaxIdRFC(u.getTax_id()))
			throw new IllegalArgumentException("Invalid tax_id format (RFC)");
		if (!ValidationUtil.validPhoneAndresFormat(u.getPhone()))
			throw new IllegalArgumentException("Invalid phone AndresFormat");
		if (repo.existsByTaxId(u.getTax_id()))
			throw new IllegalArgumentException("tax_id must be unique");
		u.setId(UUID.randomUUID());
		u.setCreated_at(nowMadagascar());
		// encrypt password plaintext -> caller should pass plaintext
		u.setPasswordEncrypted(AESUtil.encrypt(u.getPasswordEncrypted()));
		repo.save(u);
		return u;
	}

	public User patch(UUID id, Map<String, Object> updates) {
		Optional<User> ou = repo.findById(id);
		if (!ou.isPresent())
			throw new NoSuchElementException("User not found");
		User u = ou.get();
		if (updates.containsKey("email"))
			u.setEmail((String) updates.get("email"));
		if (updates.containsKey("name"))
			u.setName((String) updates.get("name"));
		if (updates.containsKey("phone")) {
			String ph = (String) updates.get("phone");
			if (!ValidationUtil.validPhoneAndresFormat(ph))
				throw new IllegalArgumentException("Invalid phone format");
			u.setPhone(ph);
		}
		if (updates.containsKey("password")) {
			u.setPasswordEncrypted(AESUtil.encrypt((String) updates.get("password")));
		}
		if (updates.containsKey("tax_id")) {
			String t = (String) updates.get("tax_id");
			if (!ValidationUtil.validTaxIdRFC(t))
				throw new IllegalArgumentException("Invalid tax_id format");
			if (!t.equals(u.getTax_id()) && repo.existsByTaxId(t))
				throw new IllegalArgumentException("tax_id must be unique");
			u.setTax_id(t);
		}
		// addresses update omitted for brevity
		return u;
	}

	public void delete(UUID id) {
		repo.delete(id);
	}

	public boolean authenticate(String taxId, String password) {
		Optional<User> ou = repo.findByTaxId(taxId);
		if (!ou.isPresent())
			return false;
		User u = ou.get();
		String encryptedProvided = AESUtil.encrypt(password);
		return encryptedProvided.equals(u.getPasswordEncrypted());
	}
}
