/**
 * 
 */
package pe.com.logistica.negocio.util;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pe.com.logistica.bean.Util.UtilProperties;

/**
 * @author edwreb
 *
 */
public class UtilCorreo {

	private final Properties properties = new Properties();
    private Session session;
	/**
	 * 
	 */
	public UtilCorreo() {
		Properties prop = UtilProperties.cargaArchivo("correoconfiguracion.properties");
		
		inicializador(prop);
	}
	
	private void inicializador(Properties propiedades){
		properties.put("mail.smtp.host", propiedades.getProperty("smtp.host"));
        properties.put("mail.smtp.starttls.enable", propiedades.getProperty("smtp.starttls.enable"));
        properties.put("mail.smtp.port", propiedades.getProperty("smtp.port"));
        properties.put("mail.smtp.mail.sender", propiedades.getProperty("smtp.mail.sender"));
        properties.put("mail.smtp.password", propiedades.getProperty("smtp.password"));
        properties.put("mail.smtp.user", propiedades.getProperty("smtp.user"));
        properties.put("mail.smtp.auth", propiedades.getProperty("smtp.auth"));
        session = Session.getDefaultInstance(properties);
	}

	public void enviarCorreo(String correoDestino, String asunto, String mensaje){
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDestino));
			message.setSubject(asunto);
			message.setText(mensaje);
			Transport t = session.getTransport("smtp");
			t.connect((String) properties.get("mail.smtp.user"), (String) properties.get("mail.smtp.password"));
			t.sendMessage(message, message.getAllRecipients());
			t.close();
			
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarCorreo(String correoDestino, String asunto, String mensaje, InputStream archivoAdjunto){
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDestino));
			message.setSubject(asunto);
			message.setText(mensaje);
			Transport t = session.getTransport("smtp");
			t.connect((String) properties.get("mail.smtp.user"), (String) properties.get("mail.smtp.password"));
			t.sendMessage(message, message.getAllRecipients());
			t.close();
			
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
