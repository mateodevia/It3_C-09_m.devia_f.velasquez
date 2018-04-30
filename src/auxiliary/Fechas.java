package auxiliary;

import java.sql.Date;

public class Fechas {

	public static String pasarDateAFormatoSQL( Date pFecha ) {
		
		String fecha = ""+pFecha.getDate() + "/" + (pFecha.getMonth()+1) + "/" + (pFecha.getYear()-100);
		
		return fecha;
	}
}
