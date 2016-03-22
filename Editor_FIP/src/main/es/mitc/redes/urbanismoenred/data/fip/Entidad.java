//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.04 at 03:59:02 PM CEST 
//


package es.mitc.redes.urbanismoenred.data.fip;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Entidad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Entidad">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="GEOMETRIA" type="{}Geometria" minOccurs="0"/>
 *         &lt;element name="HIJAS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ENTIDAD" type="{}Entidad" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DOCUMENTOS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DOCUMENTO" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="codigo" use="required" type="{}IdentificadorDocumento" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BASE" type="{}CodigoEntidad" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="codigo" use="required" type="{}IdentificadorEntidad" />
 *       &lt;attribute name="etiqueta" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="clave" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nombre" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="suspendida" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Entidad", propOrder = {
    "geometria",
    "hijas",
    "documentos",
    "base"
})
public class Entidad {

    @XmlElement(name = "GEOMETRIA")
    protected String geometria;
    @XmlElement(name = "HIJAS")
    protected Entidad.HIJAS hijas;
    @XmlElement(name = "DOCUMENTOS")
    protected Entidad.DOCUMENTOS documentos;
    @XmlElement(name = "BASE")
    protected CodigoEntidad base;
    @XmlAttribute(name = "codigo", required = true)
    protected String codigo;
    @XmlAttribute(name = "etiqueta")
    protected String etiqueta;
    @XmlAttribute(name = "clave", required = true)
    protected String clave;
    @XmlAttribute(name = "nombre", required = true)
    protected String nombre;
    @XmlAttribute(name = "suspendida", required = true)
    protected boolean suspendida;

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
     * Gets the value of the hijas property.
     * 
     * @return
     *     possible object is
     *     {@link Entidad.HIJAS }
     *     
     */
    public Entidad.HIJAS getHIJAS() {
        return hijas;
    }

    /**
     * Sets the value of the hijas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Entidad.HIJAS }
     *     
     */
    public void setHIJAS(Entidad.HIJAS value) {
        this.hijas = value;
    }

    /**
     * Gets the value of the documentos property.
     * 
     * @return
     *     possible object is
     *     {@link Entidad.DOCUMENTOS }
     *     
     */
    public Entidad.DOCUMENTOS getDOCUMENTOS() {
        return documentos;
    }

    /**
     * Sets the value of the documentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Entidad.DOCUMENTOS }
     *     
     */
    public void setDOCUMENTOS(Entidad.DOCUMENTOS value) {
        this.documentos = value;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link CodigoEntidad }
     *     
     */
    public CodigoEntidad getBASE() {
        return base;
    }

    /**
     * Sets the value of the base property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoEntidad }
     *     
     */
    public void setBASE(CodigoEntidad value) {
        this.base = value;
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
     * Gets the value of the etiqueta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Sets the value of the etiqueta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEtiqueta(String value) {
        this.etiqueta = value;
    }

    /**
     * Gets the value of the clave property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClave() {
        return clave;
    }

    /**
     * Sets the value of the clave property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClave(String value) {
        this.clave = value;
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
     * Gets the value of the suspendida property.
     * 
     */
    public boolean isSuspendida() {
        return suspendida;
    }

    /**
     * Sets the value of the suspendida property.
     * 
     */
    public void setSuspendida(boolean value) {
        this.suspendida = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DOCUMENTO" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="codigo" use="required" type="{}IdentificadorDocumento" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "documento"
    })
    public static class DOCUMENTOS {

        @XmlElement(name = "DOCUMENTO", required = true)
        protected List<Entidad.DOCUMENTOS.DOCUMENTO> documento;

        /**
         * Gets the value of the documento property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the documento property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDOCUMENTO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Entidad.DOCUMENTOS.DOCUMENTO }
         * 
         * 
         */
        public List<Entidad.DOCUMENTOS.DOCUMENTO> getDOCUMENTO() {
            if (documento == null) {
                documento = new ArrayList<Entidad.DOCUMENTOS.DOCUMENTO>();
            }
            return this.documento;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="codigo" use="required" type="{}IdentificadorDocumento" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class DOCUMENTO {

            @XmlAttribute(name = "codigo", required = true)
            protected String codigo;

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

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ENTIDAD" type="{}Entidad" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entidad"
    })
    public static class HIJAS {

        @XmlElement(name = "ENTIDAD", required = true)
        protected List<Entidad> entidad;

        /**
         * Gets the value of the entidad property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entidad property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getENTIDAD().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Entidad }
         * 
         * 
         */
        public List<Entidad> getENTIDAD() {
            if (entidad == null) {
                entidad = new ArrayList<Entidad>();
            }
            return this.entidad;
        }

    }

}