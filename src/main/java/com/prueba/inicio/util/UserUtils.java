package com.prueba.inicio.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserUtils {
	
	  public static String nowMadagascar() {
	        ZoneId zoneMadagascar = ZoneId.of("Indian/Antananarivo");
	        ZonedDateTime now = ZonedDateTime.now(zoneMadagascar);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	        return now.format(formatter);
	    }

}
