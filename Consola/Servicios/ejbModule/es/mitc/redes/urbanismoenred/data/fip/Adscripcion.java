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
 * El planeamiento define relaciones entre entidades que no son directamente obtenibles mediante relaciones topológicas o bien establece relaciones obtenibles topológicamente pero asocia a dicha relación más información que la simplemente posicional.
 * Las Adscripciones se definen como relaciones entre dos entidades que tienen el siguiente contenido informativo:
 *        - Unidad
 *        - Tipo
 *        - Cuantía
 * 
 * <p>Java class for Adscripcion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Adscripcion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PROPIEDADES" type="{}PropiedadesAdscripcion"/>
 *       &lt;/sequence>
 *       &lt;attribute name="entidadOrigen" use="required" type="{}IdentificadorEntidad" />
 *       &lt;attribute name="entidadDestino" use="required" type="{}IdentificadorEntidad" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Adscripcion", propOrder = {
    "propiedades"
})
public class Adscripcion {

    @XmlElement(name = "PROPIEDADES", required = true)
    protected PropiedadesAdscripcion propiedades;
    @XmlAttribute(name = "entidadOrigen", required = true)
    protected String entidadOrigen;
    @XmlAttribute(name = "entidadDestino", required = true)
    protected String entidadDestino;

    /**
     * Gets the value of the propiedades property.
     * 
     * @return
     *     possible object is
     *     {@link PropiedadesAdscripcion }
     *     
     */
    public PropiedadesAdscripcion getPROPIEDADES() {
        return propiedades;
    }

    /**
     * Sets the value of the propiedades property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropiedadesAdscripcion }
     *     
     */
    public void setPROPIEDADES(PropiedadesAdscripcion value) {
        this.propiedades = value;
    }

    /**
     * Gets the value of the entidadOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadOrigen() {
        return entidadOrigen;
    }

    /**
     * Sets the value of the entidadOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadOrigen(String value) {
        this.entidadOrigen = value;
    }

    /**
     * Gets the value of the entidadDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadDestino() {
        return entidadDestino;
    }

    /**
     * Sets the value of the entidadDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadDestino(String value) {
        this.entidadDestino = value;
    }

}
