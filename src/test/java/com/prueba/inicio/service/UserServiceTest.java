package com.prueba.inicio.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.prueba.inicio.model.User;
import com.prueba.inicio.repo.InMemoryUserRepository;

public class UserServiceTest {
	
	 private InMemoryUserRepository repo;
	    private UserService service;

	    @BeforeEach
	    void setup() {
	        repo = Mockito.mock(InMemoryUserRepository.class);
	        service = new UserService();
	    }

	    @Test
	    void testCreateUser_ok() {
	        User u = new User();
	        u.setId(UUID.randomUUID());
	        u.setEmail("new@mail.com");
	        u.setName("new");
	        u.setPhone("5551234567");
	        u.setTax_id("ABCD900101XXX");

	        when(repo.existsByTaxId(u.getTax_id())).thenReturn(false);

	        service.create(u);

	        verify(repo, times(1)).save(any(User.class));
	    }

	    @Test
	    void testCreateUser_failDuplicateTaxId() {
	        User u = new User();
	        u.setId(UUID.randomUUID());
	        u.setEmail("dup@mail.com");
	        u.setName("dup");
	        u.setPhone("5559876543");
	        u.setTax_id("AARR990101XXX");

	        when(repo.existsByTaxId(u.getTax_id())).thenReturn(true);

	        assertThrows(IllegalArgumentException.class, () -> service.create(u));
	    }

}
