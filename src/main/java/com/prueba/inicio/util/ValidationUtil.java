package com.prueba.inicio.util;

import java.util.regex.Pattern;

public class ValidationUtil {
	
	  private static final Pattern RFC_PATTERN = Pattern.compile("^[A-Z&Ñ]{3,4}\\d{6}[A-Z0-9]{3}$");

	    public static boolean validTaxIdRFC(String taxId) {
	        if (taxId == null) return false;
	        return RFC_PATTERN.matcher(taxId).matches();
	    }

	   
	    public static boolean validPhoneAndresFormat(String phone) {
	        if (phone == null) return false;
	        String digits = phone.replaceAll("\\D", "");
	        return digits.length() >= 10 && digits.substring(digits.length() - 10).matches("\\d{10}");
	    }

}
