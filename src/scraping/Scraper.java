package scraping;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;



import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scraper {
	private Notificacion noti = new Notificacion();
	public void Scraper() { //Constructor
		Notificacion noti = new Notificacion();
	}
	
	public String reclasificasion(String equipo) { // mina datos de la tabla de reclasificación
		int filaE=0; //indice de la fila donde se encontro el equipo
		Element coincidencia=null; // fila del equipo
		ArrayList<String> titulosl = new ArrayList<>();// titulos de la tabla
		String imagensrc="";//link de la imagen del escudo del equipo
		ArrayList<String> cont = new ArrayList<>();//celdas de la tabla
		boolean encontrado = false;
		String Reclasificacion = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "<style>\r\n" //inicio html tabla
				+ "table, th, td {\r\n" + "  border: 1px solid black;\r\n" + "}\r\n" + "</style>\r\n" + "</head>\r\n"
				+ "<body>\r\n"+"<h1 align = \"center\">Reclasificación</h1>" + "<table align = \"center\">\r\n<tr>"; 
		try {
			Document doc = Jsoup.connect("https://www.futbolred.com/tabla-de-posiciones/liga-aguila").get();//Html donde esta la tabla
			Elements tablas = doc.getElementsByTag("table");//separo la tabla del resto del html
			Element tabla = tablas.get(1);// consigo la segunda tabla ya que la del index 0 no es útil
			Elements filas = tabla.getElementsByTag("tr"); //separo la tabla por filas
			Elements titulos = null;
			for (Element fila : filas) { // recorro todas las filas una por una
				Elements celdas = fila.getElementsByTag("td"); //separo las celdas de la fila sobre la que estoy iterando
				if(titulos==null)
				{
					titulos = fila.getElementsByTag("th");//consigo los titulos de la tabla
					for(Element titulo : titulos ) {
						titulosl.add(titulo.text());//añado titulo por titulo al array
					}
				}
				for (Element celda : celdas) {//recorro celda por celda de cada fila
						if(celda.text().compareTo(equipo)==0) {//busco el nombre del equipo
							filaE = filas.indexOf(fila);//consigo el index de la fila donde aparecio
							coincidencia = filas.get(filaE); //asigno la fila
							Elements imagen = coincidencia.getElementsByTag("img");//consigo la imagen del escudo
							imagensrc = imagen.get(0).attr("src");//saco el link que esta en el atributo "src"
							encontrado = true;
						}
				}
			}
			if (encontrado) {
				Elements celdas = coincidencia.getElementsByTag("td");// separo celdas de la fila
				for (Element celda : celdas) {
					cont.add(celda.text());// añado la celdas
				}
			}
			if (!cont.isEmpty()) {
				for (int i = 0; i < titulosl.size(); i++) {
					Reclasificacion += "\r\n<th>" + titulosl.get(i) + "</th>\r\n";// agrego los titulos de la tabla al
																					// html
				}
				Reclasificacion += "\r\n <tr>\r\n";// abro fila
				for (int i = 0; i < cont.size(); i++) {
					if (i == 1) {
						Reclasificacion += "\r\n<td><img src=" + imagensrc + ">"// añado la imagen del escudo
								+ cont.get(i) + "</td>\r\n";
					} else {
						Reclasificacion += "\r\n<td>" + cont.get(i) + "</td>\r\n";// agrego celda por celda
					}
				}
			} else {
				Reclasificacion += "<p align =\"center\"> su equipo no está clasificado</p>";
			}
			Reclasificacion += "  </tr>\r\n" + "</table>\r\n" + "</body>\r\n" + "</html>";//cierro fila, tabla y html
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Reclasificacion;
	}

	public String partidos(String equipo) {//mina calendario de los partidos
		String sub="";
		int filaE=0; //indice de la fila donde se encontro el equipo
		String temp="";//Almacena temporalmente el titulo de la zona de la tabla que se esta recorriendo
		String comparar = ""; //Almacena el valor del titulo actual en la iteracion
		String plantilla = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "</head>\r\n" + "<style>\r\n"
				+ "table, th, td {\r\n" + "  border: 1px solid black;\r\n" + "}\r\n" + "</style>\r\n" + "<body>"
				+"<h1 align = \"center\">Partidos</h1>"+ "<table align = \"center\">\r\n";
		ArrayList <String> ci= new ArrayList<>();//array para guardar las coincidencias
		ArrayList <String> temps= new ArrayList<>();//para guardar titulos
		ArrayList <String> cont= new ArrayList<>();//para guardar celdas
		ArrayList <String> imgs= new ArrayList<>();//guardar imagenes
		int comp = 0;
		try {
			Document doc = Jsoup.connect("https://www.futbolred.com/partidos/liga-aguila").get();
			Elements tablas = doc.getElementsByTag("table");//Tabla partidos
			for (Element tabla : tablas) {
				Elements filas = tabla.getElementsByTag("tr");//separar filas
				for (Element fila : filas) {
					Elements celdas = fila.getElementsByTag("td");//separar celdas
					Elements titulos = fila.getElementsByTag("th");//separar titulos
					for (Element titulo : titulos) {//recorre titulos por fila
						if(titulo.equals(titulos.get(0))) {
							comparar="";//se limpia comparar
						}
						comparar += titulo.text()+" ";//guarda titulos de la fila
					}
					if (!comparar.isEmpty() || comparar.compareTo(temp)!=0) {//si no esta vacio o es diferente al anterior se toma como titulo temporal
						temp=comparar;
					}
					for (Element celda : celdas) {
						if(celda.text().contains(equipo)) {//busco el nombre del equipo
							filaE = filas.indexOf(fila);//consigo el index de la fila donde aparecio
							if(temp.contains("HORA")) {//si el titulo temporal contiene este string
								sub=temp;//se asigna a un string temporal
								int a = sub.indexOf("ESTADIO");//busco el index donde empieza ESTADIO
								int b = sub.indexOf("HORA");//y HORA
								sub = sub.substring(b+5, a-1);//al temporal le asigno un sub string quitando ESTADIO y HORA
								temps.add(sub);//add a array de titulos
							}else {
							temps.add(temp);//add titulo temporal al array de titulos
							}
							break;
						}
					}
					Element coincidencia = filas.get(filaE);
					Elements imagenes = coincidencia.getElementsByTag("img");// imagenes de la fila donde esta el equipo
					Elements celdasc = coincidencia.getElementsByTag("td");
					if(comp!= filaE) {
						for(Element img : imagenes) {
							imgs.add(img.attr("src"));//añado el link de las imagenes uno por uno
						}						
					}
					for(Element celda: celdasc) {
						if (comp != filaE) {
							if(celda.text().contains("Ver minuto a minuto")) {//si la celda dice ver minuto a minuto
								sub = celda.text();
								int a =sub.indexOf("V");
								sub = sub.substring(0, a-1);//conservo el sub string sin "Ver minuto a minuto"
								cont.add(sub);
							}else {
							cont.add(celda.text());//de otra manera conservo el contenido de la celda tal y como esta
							}
						}
					}
					comp = filaE;//int para no guardar varias veces la misma fila
				}
			}
			for(int i=0;i<temps.size();i++) {
				if (cont.size() > 0 ) {
					plantilla += "<tr><th>Hora</th> <th>" + temps.get(i)//completo el HTML con que se almaceno en los arrays
							+ "</th><th>Estadio</th> </tr>  <tr>" + "<td>" + cont.get(i * 3) + "</td>\r\n"//se multiplica por 3 porq siempre habran 3 celdas por fila
							+ "    <td width =\"330\">\r\n" + "<div align=\"center\"> \r\n" + "<img src=" + imgs.get(2 * i) + ">\r\n"//se multiplica por 2 porq siempre habran dos imagenes por fila
							+ cont.get(3 * i + 1) + "\r\n" + "\r\n" + "<img src=" + imgs.get(2 * i + 1) + ">\r\n "
							+ "</div>\r\n" + "</td>\r\n" + "<td>" + cont.get(3 * i + 2) + "</td>\r\n" + "</tr>\r\n";
							
				}
			}
			
		} catch (Exception e) {
		}
		if(temps.isEmpty()) {
			plantilla +="<p align=\"center\">No hay horarios para su equipo</p>";//cierro tabla y html
		}
		plantilla +="</table>\r\n"+ "</body>\r\n" + "</html>";//cierro tabla y html
		return plantilla;
	}
	
	public String clasificacion(String equipo) {
		Document doc;
		ArrayList<String> titulos = new ArrayList<>();
		ArrayList<String> cont = new ArrayList<>();
		String img ="";
		boolean encontrado = false;
		int filaE=0;
		Element coincidencia=null;
		String clasificacion = "\r\n<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "</head>\r\n" + "<style>\r\n"
				+ "table, th, td {\r\n" + "  border: 1px solid black;\r\n" + "}</style>\r\n" + "<body>\r\n"
				+ "<h1 align = \"center\">Clasificación</h1>" + "<table align=\"center\">\r\n<tr>";
		try {
			doc = Jsoup.connect("https://aquehorajuega.co/competiciones/colombia/tabla-de-posiciones/").get();
			Elements tablas = doc.getElementsByTag("table");//separo la tabla del resto del html
			Element tabla = tablas.get(0);// consigo la segunda tabla ya que la del index 0 no es útil
			Elements filas = tabla.getElementsByTag("tr"); //separo la tabla por filas
			Elements titulo = null;
			for (Element fila : filas) { // recorro todas las filas una por una
				Elements celdas = fila.getElementsByTag("td"); //separo las celdas de la fila sobre la que estoy iterando
				Elements imagenes = fila.getElementsByTag("img");
				if(titulo==null)
				{
					titulo = fila.getElementsByTag("th");//consigo el titulo de la tabla
					for(Element t : titulo) {
						titulos.add(t.text());
					}
				}
				for (Element celda : celdas) {//recorro celda por celda de cada fila
						if(celda.text().compareTo(equipo)==0) {//busco el nombre del equipo
							filaE = filas.indexOf(fila);//consigo el index de la fila donde aparecio
							coincidencia = filas.get(filaE); //asigno la fila
							img = imagenes.get(0).attr("src");//link imagen del escudo
							encontrado = true;
						}
				}
			}
			if (encontrado) {
				Elements celdas = coincidencia.getElementsByTag("td");// separo celdas de la fila
				for (Element celda : celdas) {
					cont.add(celda.text());
				}
			}
			if (!cont.isEmpty()) {
				for (int i = 0; i < titulos.size(); i++) {
					if (i == 1) {
						clasificacion += "<th> </th>";
					}
					clasificacion += "<th>" + titulos.get(i) + "</th>";
				}
				clasificacion += "</tr>\r\n<tr>";
				for (int i = 0; i < cont.size(); i++) {
					if (i == 1) {
						clasificacion += "<td>" + "<img src=" + img + " alt=\"\">" + "</td>";
					}
					clasificacion += "<td>" + cont.get(i) + "</td>";
				}
			} else {
				clasificacion += "<p align =\"center\"> su equipo no está clasificado</p>";
			}
			clasificacion += "</tr>\r\n" + "</table>\r\n" + "</body>\r\n" + "</html>";
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clasificacion;
	}
	public String minarArt(String eq) {
		Calendar cal = Calendar.getInstance();//consegui automaticamente la fecha actual
		String fecha ="";//para almacenar la fecha como string
		String equipo = eq;
		String plantilla ="\r\n<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "</head>\r\n" + "<style>\r\n"
				+ "table, th, td {\r\n" + "  border: 1px solid black;\r\n" + "}\r\n"
				+ "</style>\r\n<body>"+"<h1 align = \"center\">Artículos</h1>"+"<table align=\"center\">\r\n" ;
		ArrayList <String> ltitulos = new ArrayList<>();
		ArrayList <String> lcontenidos = new ArrayList<>();
		ArrayList <String> lLinks = new ArrayList<>();//hipervinculos de las noticias
		ArrayList <String> imgs = new ArrayList<>();//imagenes

		//ya q la fecha solo se recibe en el siguiente formato YY/MM//DD los condicionales la modifican a como lo trae Calendar
		if (cal.get(Calendar.MONTH) > 9 && cal.get(Calendar.DAY_OF_MONTH)>9) {
			fecha = String.valueOf(cal.get(Calendar.YEAR)).substring(2) + "-" + String.valueOf(cal.get(Calendar.MONTH))
					+ "-" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		}
		if(cal.get(Calendar.MONTH)<10 && cal.get(Calendar.DAY_OF_MONTH)<10) {
			fecha = String.valueOf(cal.get(Calendar.YEAR)).substring(2) + "-" + "0"+String.valueOf(cal.get(Calendar.MONTH))
			+ "-" +"0"+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		}
		if(cal.get(Calendar.MONTH)>9 && cal.get(Calendar.DAY_OF_MONTH)<10) {
			fecha = String.valueOf(cal.get(Calendar.YEAR)).substring(2) + "-" + String.valueOf(cal.get(Calendar.MONTH))
			+ "-" +"0"+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		}
		if(cal.get(Calendar.MONTH)<10 && cal.get(Calendar.DAY_OF_MONTH)>9) {
			fecha = String.valueOf(cal.get(Calendar.YEAR)).substring(2) + "-" + "0"+String.valueOf(cal.get(Calendar.MONTH))
			+ "-" +String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		}
		try {
			Document doc = Jsoup.connect("https://www.futbolred.com/buscar?q="+equipo+"&publishedAt%5Bfrom%5D="+fecha+"&publishedAt%5Buntil%5D="+fecha+"&contentTypes%5B0%5D=article").get();
			Elements articulos  = doc.getElementsByTag("article");
			for (Element art : articulos) {
				Elements imagenes = art.getElementsByClass("image page-link");
				Elements titulos = art.getElementsByTag("h2");
				Elements links = art.getElementsByClass("title page-link");
				Elements contenidos = art.getElementsByTag("p");
				for(Element imagen : imagenes) {
					Elements img = imagen.getElementsByTag("img");
					if(!art.equals(articulos.get(0))) {
					imgs.add("https://www.futbolred.com"+img.attr("src"));//url de la imagen String
					}
				}
				for(Element titulo : titulos) {
					if(!art.equals(articulos.get(0))) {//el elemento 0 no es util se valida q no se guarde
						ltitulos.add(titulo.text());
					}
				}
				for(Element cont : contenidos) {
					if (!art.equals(articulos.get(0))) {
						lcontenidos.add(cont.text());
					}
				}
				for(Element link : links) {
					if(!art.equals(articulos.get(0))) {
						lLinks.add("https://www.futbolred.com"+link.attr("href"));
					}
				}
				if (!art.equals(articulos.get(0))) {
					plantilla += "  <tr>\r\n" + "   <th>\r\n"
							+ ltitulos.get(ltitulos.size() - 1) + "    </th>\r\n" + "  </tr>\r\n" + "  <tr>\r\n"
							+ "    <th> <img src=" + imgs.get(imgs.size() - 1) + " alt=\"\"></th>\r\n" + "  </tr>\r\n"
							+ "  <tr>\r\n" + "    <td>" + lcontenidos.get(lcontenidos.size() - 1) + "</td>\r\n"
							+ "  </tr>\r\n" + "   <tr>\r\n" + "    <td>" + lLinks.get(lLinks.size() - 1) + "</td>\r\n"
							+ "  </tr>\r\n";
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plantilla += "</table>" + "</body>\r\n" + "</html>";
		if(ltitulos.isEmpty()) {
			return plantilla + "\r\n <p align = \"center\">No hay articulos disponibles hoy<p>";
		}else {
		return plantilla;
		}
	}
	public void crearNotificacion(String equipo) {
		this.noti.setCont(this.clasificacion(equipo) + this.reclasificasion(equipo) +this.partidos(equipo)+ this.minarArt(equipo));
		System.out.println(noti.getCont());
	}
	public Notificacion getNotificacion() {
		return this.noti;
	}
	public static void main(String[] args) {
		Scraper a = new Scraper();
		a.crearNotificacion("Pereira");
	}
}