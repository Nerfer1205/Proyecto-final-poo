package scraping;

import java.io.Serializable;
import java.util.Calendar;

public class Notificacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public String getCont() {
		return this.cont;
	}
	public String getFecha() {
		return this.fecha;
	}
}
 