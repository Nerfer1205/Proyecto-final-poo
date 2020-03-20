package scraping;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	public void Scraper() { //Constructor
		
	}
	
	public void reclasificasion(String equipo) { // mina datos de la tabla de reclasificación
		int filaE=0; //indice de la fila donde se encontro el equipo
		Element coincidencia=null; // fila del equipo
		String filac=""; //fila del equipo en String
		String tituloS=""; //Titulo de la tabla
		String Reclasificacion; //Titulo con fila
		try {
			Document doc = Jsoup.connect("https://www.futbolred.com/tabla-de-posiciones/liga-aguila").get();//Html donde esta la tabla
			Elements tablas = doc.getElementsByTag("table");//separo la tabla del resto del html
			Element tabla = tablas.get(1);// consigo la segunda tabla ya que la del index 0 no es útil
			Elements filas = tabla.getElementsByTag("tr"); //separo la tabla por filas
			Elements titulo = null;
			for (Element fila : filas) { // recorro todas las filas una por una
				Elements celdas = fila.getElementsByTag("td"); //separo las celdas de la fila sobre la que estoy iterando
				if(titulo==null)
				{
					titulo = fila.getElementsByTag("th");//consigo el titulo de la tabla
					tituloS = titulo.text();
				}
				for (Element celda : celdas) {//recorro celda por celda de cada fila
						if(celda.text().compareTo(equipo)==0) {//busco el nombre del equipo
							filaE = filas.indexOf(fila);//consigo el index de la fila donde aparecio
							coincidencia = filas.get(filaE); //asigno la fila
						}
				}
			}
			Elements celdas = coincidencia.getElementsByTag("td");//separo celdas de la fila 
			for(Element celda:celdas) {
				filac += celda.text()+"  ";//guardo las celdas con interespaceado
			}
			Reclasificacion=  tituloS+"\n"+filac;//junto los dos String
			System.out.println(Reclasificacion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void partidos(String equipo) {//mina calendario de los partidos
		int filaE=0; //indice de la fila donde se encontro el equipo
		String temp="";//Almacena temporalmente el titulo de la zona de la tabla que se esta recorriendo
		String comparar = ""; //Almacena el valor del titulo actual en la iteracion
		ArrayList <String> ci= new ArrayList<>();//array para guardar las coincidencias
		try {
			Document doc = Jsoup.connect("https://www.futbolred.com/partidos/liga-aguila").get();//HTML partidos
			Elements tablas = doc.getElementsByTag("table");//Tabla partidos
			for (Element tabla : tablas) {
				Elements filas = tabla.getElementsByTag("tr");//separar filas
				for (Element fila : filas) {
					Elements celdas = fila.getElementsByTag("td");//separar celdas
					Elements titulos = fila.getElementsByTag("th");//separar titulos
					for (Element titulo : titulos) {//recorre titulos por fila
						comparar += titulo.text()+" ";//guarda titulos de la fila
						if (comparar.isBlank() || comparar.compareTo(temp)!=0) {//si no esta vacio o es diferente al anterior se guarda
							temp=comparar;
						}
					}
					comparar="";//se limpia comparar
					for (Element celda : celdas) {
						if(celda.text().contains(equipo)) {//busco el nombre del equipo
							filaE = filas.indexOf(fila);//consigo el index de la fila donde aparecio
							System.out.println(temp+"\n"+filas.get(filaE).text());
							ci.add(temp+"\n"+filas.get(filaE).text());//se añade la fila con su respectivo titulo
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clasificacion(String equipo) {
		Document doc;
		int filaE=0;
		String tituloS="";
		String filac="";
		Element coincidencia=null;
		String clasificacion;
		try {
			doc = Jsoup.connect("https://aquehorajuega.co/competiciones/colombia/tabla-de-posiciones/").get();
			Elements tablas = doc.getElementsByTag("table");//separo la tabla del resto del html
			Element tabla = tablas.get(0);// consigo la segunda tabla ya que la del index 0 no es útil
			Elements filas = tabla.getElementsByTag("tr"); //separo la tabla por filas
			Elements titulo = null;
			for (Element fila : filas) { // recorro todas las filas una por una
				Elements celdas = fila.getElementsByTag("td"); //separo las celdas de la fila sobre la que estoy iterando
				if(titulo==null)
				{
					titulo = fila.getElementsByTag("th");//consigo el titulo de la tabla
					tituloS = titulo.text();
				}
				for (Element celda : celdas) {//recorro celda por celda de cada fila
						if(celda.text().compareTo("equipo")==0) {//busco el nombre del equipo
							filaE = filas.indexOf(fila);//consigo el index de la fila donde aparecio
							coincidencia = filas.get(filaE); //asigno la fila
						}
				}
			}
			Elements celdas = coincidencia.getElementsByTag("td");//separo celdas de la fila 
			for(Element celda:celdas) {
				filac += celda.text()+"  ";//guardo las celdas con interespaceado
			}
			clasificacion=  tituloS+"\n"+filac;//junto los dos String
			System.out.println(clasificacion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		String fecha ="";
		String equipo = "nacional";
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
		fecha="20-03-04";
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
					img.attr("src");//url de la imagen String
					//<img src="/files/listing_default/uploads/2019/06/19/5d0af44d691aa.jpeg" alt="">
				}
				for(Element titulo : titulos) {
					if(!art.equals(articulos.get(0))) {
//						System.out.println(titulo.text()+"*");
					}
				}
				for(Element cont : contenidos) {
					if (!art.equals(articulos.get(0))) {
//						System.out.println(cont.text());
//						System.out.println();
					}
				}
				for(Element link : links) {
					System.out.println(link.attr("href"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}