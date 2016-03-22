//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.04 at 03:59:02 PM CEST 
//


package es.mitc.redes.urbanismoenred.data.fip;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Código Universal de Determinación.
 * 
 * Estaró formado por un Identificador de trómite y un Identificador de Determinación.
 * 
 * 
 * 
 * <p>Java class for CodigoDeterminacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CodigoDeterminacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="tramite" use="required" type="{}IdentificadorTramite" />
 *       &lt;attribute name="determinacion" use="required" type="{}IdentificadorDeterminacion" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodigoDeterminacion")
@XmlSeeAlso({
    es.mitc.redes.urbanismoenred.data.fip.Determinacion.UNIDAD.class
})
public class CodigoDeterminacion {

    @XmlAttribute(name = "tramite", required = true)
    protected String tramite;
    @XmlAttribute(name = "determinacion", required = true)
    protected String determinacion;

    /**
     * Gets the value of the tramite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTramite() {
        return tramite;
    }

    /**
     * Sets the value of the tramite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTramite(String value) {
        this.tramite = value;
    }

    /**
     * Gets the value of the determinacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeterminacion() {
        return determinacion;
    }

    /**
     * Sets the value of the determinacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeterminacion(String value) {
        this.determinacion = value;
    }

}