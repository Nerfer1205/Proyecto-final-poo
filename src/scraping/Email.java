package scraping;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	private Scraper scraper;
	
	public Email() {
		// TODO Auto-generated constructor stub
		scraper = new Scraper();
	}
	 public  void enviar(String cont) {

	        final String username = "matchfinder.poo@gmail.com";
	        final String password = "ehmpiqryjdzgkcfu";

	        Properties prop = new Properties();
			prop.put("mail.smtp.host", "smtp.gmail.com");
	        prop.put("mail.smtp.port", "465");
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.socketFactory.port", "465");
	        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        
	        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, password);
	                    }
	                });

	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress("from@gmail.com"));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse("mile.120501@gmail.com, angulocita@gmail.com")
	            );
	            message.setSubject("Probando correo Gmail SSL");
	            message.setContent(cont,"text/html");

	            Transport.send(message);

	            System.out.println("Done");

	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }
	 public static void main(String[] args) {
		Email mail = new Email();
		mail.scraper.crearNotificacion("Millonarios");
		mail.enviar(mail.scraper.getNotificacion().getCont());
	}

}
