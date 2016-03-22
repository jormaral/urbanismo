
package es.mitc.redes.urbanismoenred.consola.visor.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fechaRefundido complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fechaRefundido">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idAmbito" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fechaRefundido", propOrder = {
    "idAmbito"
})
public class FechaRefundido {

    protected int idAmbito;

    /**
     * Gets the value of the idAmbito property.
     * 
     */
    public int getIdAmbito() {
        return idAmbito;
    }

    /**
     * Sets the value of the idAmbito property.
     * 
     */
    public void setIdAmbito(int value) {
        this.idAmbito = value;
    }

}
