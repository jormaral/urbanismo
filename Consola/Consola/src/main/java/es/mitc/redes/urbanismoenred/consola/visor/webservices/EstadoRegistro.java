
package es.mitc.redes.urbanismoenred.consola.visor.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for estadoRegistro complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="estadoRegistro">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idAmbito" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idioma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estadoRegistro", propOrder = {
    "idAmbito",
    "idioma"
})
public class EstadoRegistro {

    protected int idAmbito;
    protected String idioma;

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

    /**
     * Gets the value of the idioma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Sets the value of the idioma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdioma(String value) {
        this.idioma = value;
    }

}
