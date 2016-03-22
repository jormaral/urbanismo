package es.mitc.redes.urbanismoenred.servicios.seguridad;

import es.mitc.redes.urbanismoenred.servicios.comunes.LeerXML;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;


import org.w3c.dom.*;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;

/**
 *
 * @author Javier.Molina
 */
public class CreateSignatureXML {

    private static final String PROPERTIES = "validacion";

    private final String KEYSTORE_TYPE = Textos.getTexto(PROPERTIES,"keystore.type");
    private static final String KEYSTORE_FILE = Textos.getTexto(PROPERTIES,"keystore.file");
    private static final String KEYSTORE_PASSWORD = Textos.getTexto(PROPERTIES,"keystore.password");
    private static final String PRIVATE_KEY_PASSWORD = Textos.getTexto(PROPERTIES,"keystore.private.password");
    private static final String PRIVATE_KEY_ALIAS = Textos.getTexto(PROPERTIES,"keystore.private.alias");


    public void firma(String FileFip) throws Exception {
        org.apache.xml.security.Init.init();
        
        Document doc = LeerXML.leer(FileFip);
        /////Document doc = DOMUtils.createSampleDocument();

        Constants.setSignatureSpecNSprefix("");

        // Cargamos el almacen de claves
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
        ks.load(new FileInputStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());

        // Obtenemos la clave privada, pues la necesitaremos para encriptar.
        PrivateKey privateKey = (PrivateKey) ks.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASSWORD.toCharArray());
        File signatureFile = new File(FileFip);
        
		String baseURI = signatureFile.toURI().toString();

        XMLSignature xmlSignature = new XMLSignature(doc, baseURI, XMLSignature.ALGO_ID_SIGNATURE_DSA);

        // Añadimos el nodo de la firma a la raiz antes de firmar.
        doc.getDocumentElement().appendChild(xmlSignature.getElement());

        // Añadimos el KeyInfo del certificado cuya clave privada usamos
        X509Certificate cert = (X509Certificate) ks.getCertificate(PRIVATE_KEY_ALIAS);
        xmlSignature.addKeyInfo(cert);
        xmlSignature.addKeyInfo(cert.getPublicKey());

        // Realizamos la firma
        xmlSignature.sign(privateKey);

        // Guardamos archivo de firma en disco
       DOMUtils.outputDocToFile(doc, signatureFile);

    }
}
