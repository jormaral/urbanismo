package es.mitc.redes.urbanismoenred.servicios.seguridad;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

/**
 * Message-Driven Bean implementation class for: ConsumidorEventosCambiosBD
 *
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/CambiosBD"),
		@ActivationConfigProperty(propertyName = "destinationName", propertyValue = "jms/UrbanismoEnRedCambioBD"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")} 
		)
public class ConsumidorEventosCambiosBD implements MessageListener {
	
	private static final Logger log = Logger.getLogger(ConsumidorEventosCambiosBD.class);
	
	@EJB
	private GestorFip1Local gestorFip1;

    /**
     * Default constructor. 
     */
    public ConsumidorEventosCambiosBD() {
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        try {
			if (message.propertyExists("DatosModificados")) {
				switch (message.getStringProperty("DatosModificados")) {
					case "Diccionario":
							gestorFip1.marcarComoObsoleto(null, null);
						break;
					case "PlanBase":
					case "Refundido":
							gestorFip1.marcarComoObsoleto(message.getIntProperty("Ambito"), null);
						break;
					default:
						break;
				}
			}
		} catch (JMSException e) {
			log.warn("Error al procesar mensaje: " + e.getMessage());
		}
        
    }

}
