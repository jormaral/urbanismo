//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.04 at 03:59:02 PM CEST 
//


package es.mitc.redes.urbanismoenred.data.fip;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Utilizado en el FIP 1 como información del encargo al productor, que deberó producir un trómite del plan descrito y creado en el registro de planeamiento.
 * 
 * <p>Java class for PlanDigital complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlanDigital">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AMBITO-APLICACION" type="{}Geometria"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ambito" use="required" type="{}IdentificadorAmbito" />
 *       &lt;attribute name="nombre" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instrumento" use="required" type="{}IdentificadorInstrumento" />
 *       &lt;attribute name="iteracion" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="tipoTramite" use="required" type="{}IdentificadorTipoTramite" />
 *       &lt;attribute name="codigoTramite" use="required" type="{}IdentificadorTramite" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlanDigital", propOrder = {
    "ambitoaplicacion"
})
public class PlanDigital {

    @XmlElement(name = "AMBITO-APLICACION", required = true)
    protected String ambitoaplicacion;
    @XmlAttribute(name = "ambito", required = true)
    protected String ambito;
    @XmlAttribute(name = "nombre", required = true)
    protected String nombre;
    @XmlAttribute(name = "instrumento", required = true)
    protected String instrumento;
    @XmlAttribute(name = "iteracion", required = true)
    protected BigInteger iteracion;
    @XmlAttribute(name = "tipoTramite", required = true)
    protected String tipoTramite;
    @XmlAttribute(name = "codigoTramite", required = true)
    protected String codigoTramite;

    /**
     * Gets the value of the ambitoaplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAMBITOAPLICACION() {
        return ambitoaplicacion;
    }

    /**
     * Sets the value of the ambitoaplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAMBITOAPLICACION(String value) {
        this.ambitoaplicacion = value;
    }

    /**
     * Gets the value of the ambito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmbito() {
        return ambito;
    }

    /**
     * Sets the value of the ambito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmbito(String value) {
        this.ambito = value;
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
     * Gets the value of the instrumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrumento() {
        return instrumento;
    }

    /**
     * Sets the value of the instrumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrumento(String value) {
        this.instrumento = value;
    }

    /**
     * Gets the value of the iteracion property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIteracion() {
        return iteracion;
    }

    /**
     * Sets the value of the iteracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIteracion(BigInteger value) {
        this.iteracion = value;
    }

    /**
     * Gets the value of the tipoTramite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoTramite() {
        return tipoTramite;
    }

    /**
     * Sets the value of the tipoTramite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoTramite(String value) {
        this.tipoTramite = value;
    }

    /**
     * Gets the value of the codigoTramite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoTramite() {
        return codigoTramite;
    }

    /**
     * Sets the value of the codigoTramite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoTramite(String value) {
        this.codigoTramite = value;
    }

}