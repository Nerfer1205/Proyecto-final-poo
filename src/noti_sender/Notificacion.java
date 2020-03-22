package noti_sender;

import java.util.Calendar;

public class Notificacion {
	private String fecha;
	private String cont;
	private Calendar cal = Calendar.getInstance();
	
	public Notificacion() {
		fecha = String.valueOf(cal.get(Calendar.YEAR)) + String.valueOf(cal.get(Calendar.MONTH))
				+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		cont ="";
	}
	public void setCont(String c) {
		cont = c;
	}
}
