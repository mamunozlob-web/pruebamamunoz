package com.prueba.inicio.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidationUtilTest {
	
	@Test
    void validRFC_ok() {
        assertTrue(ValidationUtil.validTaxIdRFC("AARR990101XXX"));
    }

    @Test
    void validRFC_fail() {
        assertFalse(ValidationUtil.validTaxIdRFC("123ABC"));
    }

    @Test
    void validPhone_ok() {
        assertTrue(ValidationUtil.validPhoneAndresFormat("+52 5551234567"));
    }

    @Test
    void validPhone_fail() {
        assertFalse(ValidationUtil.validPhoneAndresFormat("12345"));
    }

}
