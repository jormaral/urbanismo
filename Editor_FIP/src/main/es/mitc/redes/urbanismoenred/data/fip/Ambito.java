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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Ambito sobre el que afecta un plan urbanístico
 * 
 * <p>Java class for Ambito complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Ambito">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="GEOMETRIA" type="{}Geometria" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="nombre" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codigo" use="required" type="{}IdentificadorAmbito" />
 *       &lt;attribute name="ine">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="2"/>
 *             &lt;maxLength value="6"/>
 *             &lt;pattern value="([0-9])+"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="tipoAmbito" use="required" type="{}IdentificadorTipoAmbito" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ambito", propOrder = {
    "geometria"
})
@XmlSeeAlso({
    es.mitc.redes.urbanismoenred.data.fip.Diccionario.AMBITOS.AMBITO.class
})
public class Ambito {

    @XmlElement(name = "GEOMETRIA")
    protected String geometria;
    @XmlAttribute(name = "nombre", required = true)
    protected String nombre;
    @XmlAttribute(name = "codigo", required = true)
    protected String codigo;
    @XmlAttribute(name = "ine")
    protected String ine;
    @XmlAttribute(name = "tipoAmbito", required = true)
    protected String tipoAmbito;

    /**
     * Gets the value of the geometria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGEOMETRIA() {
        return geometria;
    }

    /**
     * Sets the value of the geometria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGEOMETRIA(String value) {
        this.geometria = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the ine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIne() {
        return ine;
    }

    /**
     * Sets the value of the ine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIne(String value) {
        this.ine = value;
    }

    /**
     * Gets the value of the tipoAmbito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoAmbito() {
        return tipoAmbito;
    }

    /**
     * Sets the value of the tipoAmbito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoAmbito(String value) {
        this.tipoAmbito = value;
    }

}
