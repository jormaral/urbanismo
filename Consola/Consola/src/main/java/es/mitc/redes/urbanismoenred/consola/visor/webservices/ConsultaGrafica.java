
package es.mitc.redes.urbanismoenred.consola.visor.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultaGrafica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultaGrafica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wktGeometry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="srs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "consultaGrafica", propOrder = {
    "wktGeometry",
    "srs",
    "idioma"
})
public class ConsultaGrafica {

    protected String wktGeometry;
    protected String srs;
    protected String idioma;

    /**
     * Gets the value of the wktGeometry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWktGeometry() {
        return wktGeometry;
    }

    /**
     * Sets the value of the wktGeometry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWktGeometry(String value) {
        this.wktGeometry = value;
    }

    /**
     * Gets the value of the srs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrs() {
        return srs;
    }

    /**
     * Sets the value of the srs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrs(String value) {
        this.srs = value;
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
