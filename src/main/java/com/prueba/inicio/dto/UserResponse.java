package com.prueba.inicio.dto;

import java.util.List;
import java.util.UUID;

import com.prueba.inicio.model.Address;

public class UserResponse {
	private UUID id;
	private String email;
	private String name;
	private String phone;
	private String tax_id;
	private String created_at;
	private List<Address> addresses;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTax_id() {
		return tax_id;
	}
	public void setTax_id(String tax_id) {
		this.tax_id = tax_id;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	public java.util.Map<String,Object> toMap() {
		java.util.Map<String,Object> m = new java.util.HashMap<>();
		m.put("id", id); m.put("email", email); m.put("name", name);
		m.put("phone", phone); m.put("tax_id", tax_id); m.put("created_at", created_at);
		m.put("addresses", addresses);
		return m;
		}
}
