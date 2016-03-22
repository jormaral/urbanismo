/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 ** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
 * sean aprobadas por la Comision Europea- versiones
 * posteriores de la EUPL (la <<Licencia>>);
 * Solo podra usarse esta obra si se respeta la Licencia.
 * Puede obtenerse una copia de la Licencia en:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Salvo cuando lo exija la legislacion aplicable o se acuerde.
 * por escrito, el programa distribuido con arreglo a la
 * Licencia se distribuye <<TAL CUAL>>,
 * SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
 * ni implicitas.
 * Vease la Licencia en el idioma concreto que rige
 * los permisos y limitaciones que establece la Licencia.
 */
package es.mitc.redes.urbanismoenred.servicios.comunes;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * @author Arnaiz Consultores
 */
@Stateless
public class ServicioMailBean implements ServicioMailLocal {

    @PersistenceContext(unitName = "rpmv2")
    EntityManager em;
    private static final Logger log = Logger.getLogger(ServicioMailBean.class);
    private final Properties properties = new Properties();
    private String propertiesFile = "correo";
    private String password = Textos.getTexto(propertiesFile, "password");
    private Session session = null;
    private String smtp = Textos.getTexto(propertiesFile, "smtp");
    private String sender = Textos.getTexto(propertiesFile, "sender");
    private String user = Textos.getTexto(propertiesFile, "user");

    @PostConstruct
    public void init() {
        try {
            properties.put("mail.smtp.host", smtp);
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", 25);
            properties.put("mail.smtp.mail.sender", sender);
            properties.put("mail.smtp.user", user);
            properties.put("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(properties);
        } catch (Exception ex) {
            log.info("Error  obteniendo parámetros de configuración de correo. " + ex);
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.comunes.ServicioMailLocal#sendEmail(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void sendEmail(String asunto, String mensaje, String usuario) {
        String mailAdress = "";
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            if (!"".equals(usuario)) {
                String strQuery = "select cast (correo as text) from seguridad.usuario " +
                        "where nombre = '" + usuario + "'";
                mailAdress = em.createNativeQuery(strQuery).getSingleResult().toString();
            }
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAdress));
            message.setSubject(asunto);
            message.setText(mensaje + " \n " + new Date());
            Transport t = session.getTransport("smtp");
            t.connect(smtp, (String) properties.get(smtp), password);
            
            t.sendMessage(message, message.getAllRecipients());
            t.close();

            log.info("Notificación enviada por correo electrónico. ");
        } catch (MessagingException me) {
            log.error("Error mandando correo de notificación. " + me);
            return;
        }

    }
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.comunes.ServicioMailLocal#sendEmail(java.lang.String, java.lang.String, javax.mail.internet.InternetAddress)
     */
    @Override
    public void sendEmail(String asunto, String mensaje, InternetAddress destinatario) {
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, destinatario);
            message.setSubject(asunto);
            message.setText(mensaje + " \n " + new Date());
            Transport t = session.getTransport("smtp");
            t.connect(smtp, (String) properties.get(smtp), password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();

            log.info("Notificación enviada por correo electrónico a: " + destinatario.getAddress());
        } catch (MessagingException me) {
            log.error("Error mandando correo de notificación. " + me.getMessage(), me);
        }

    }
}
