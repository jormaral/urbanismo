
package es.mitc.redes.urbanismoenred.servicios.refundido;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.mitc.redes.urbanismoenred.servicios.refundido package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EsRefundibleResponse_QNAME = new QName("http://refundido.servicios.urbanismoenred.redes.mitc.es/", "esRefundibleResponse");
    private final static QName _RefundirTramites_QNAME = new QName("http://refundido.servicios.urbanismoenred.redes.mitc.es/", "refundirTramites");
    private final static QName _EsRefundible_QNAME = new QName("http://refundido.servicios.urbanismoenred.redes.mitc.es/", "esRefundible");
    private final static QName _RefundirTramitesResponse_QNAME = new QName("http://refundido.servicios.urbanismoenred.redes.mitc.es/", "refundirTramitesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.mitc.redes.urbanismoenred.servicios.refundido
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EsRefundibleResponse }
     * 
     */
    public EsRefundibleResponse createEsRefundibleResponse() {
        return new EsRefundibleResponse();
    }

    /**
     * Create an instance of {@link RefundirTramitesResponse }
     * 
     */
    public RefundirTramitesResponse createRefundirTramitesResponse() {
        return new RefundirTramitesResponse();
    }

    /**
     * Create an instance of {@link EsRefundible }
     * 
     */
    public EsRefundible createEsRefundible() {
        return new EsRefundible();
    }

    /**
     * Create an instance of {@link RefundirTramites }
     * 
     */
    public RefundirTramites createRefundirTramites() {
        return new RefundirTramites();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EsRefundibleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://refundido.servicios.urbanismoenred.redes.mitc.es/", name = "esRefundibleResponse")
    public JAXBElement<EsRefundibleResponse> createEsRefundibleResponse(EsRefundibleResponse value) {
        return new JAXBElement<EsRefundibleResponse>(_EsRefundibleResponse_QNAME, EsRefundibleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundirTramites }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://refundido.servicios.urbanismoenred.redes.mitc.es/", name = "refundirTramites")
    public JAXBElement<RefundirTramites> createRefundirTramites(RefundirTramites value) {
        return new JAXBElement<RefundirTramites>(_RefundirTramites_QNAME, RefundirTramites.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EsRefundible }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://refundido.servicios.urbanismoenred.redes.mitc.es/", name = "esRefundible")
    public JAXBElement<EsRefundible> createEsRefundible(EsRefundible value) {
        return new JAXBElement<EsRefundible>(_EsRefundible_QNAME, EsRefundible.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundirTramitesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://refundido.servicios.urbanismoenred.redes.mitc.es/", name = "refundirTramitesResponse")
    public JAXBElement<RefundirTramitesResponse> createRefundirTramitesResponse(RefundirTramitesResponse value) {
        return new JAXBElement<RefundirTramitesResponse>(_RefundirTramitesResponse_QNAME, RefundirTramitesResponse.class, null, value);
    }

}
