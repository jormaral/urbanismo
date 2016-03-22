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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Las unidades en que se pueden expresar las determinaciones son otras determinaciones con nuevos atributos:
 *        -Abreviatura
 *        -Definición
 * 
 * <p>Java class for Unidad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Unidad">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="DEFINICION" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="determinacion" use="required" type="{}IdentificadorDeterminacion" />
 *       &lt;attribute name="abreviatura" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Unidad", propOrder = {
    "definicion"
})
public class Unidad {

    @XmlElement(name = "DEFINICION")
    protected String definicion;
    @XmlAttribute(name = "determinacion", required = true)
    protected String determinacion;
    @XmlAttribute(name = "abreviatura", required = true)
    protected String abreviatura;

    /**
     * Gets the value of the definicion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEFINICION() {
        return definicion;
    }

    /**
     * Sets the value of the definicion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEFINICION(String value) {
        this.definicion = value;
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

    /**
     * Gets the value of the abreviatura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbreviatura() {
        return abreviatura;
    }

    /**
     * Sets the value of the abreviatura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbreviatura(String value) {
        this.abreviatura = value;
    }

}
