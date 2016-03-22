package es.mitc.redes.urbanismoenred.servicios.seguridad;

/**
 *
 * @author Javier.Molina
 */
import com.itextpdf.text.Rectangle;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;


public class signPDF {

    private static final String PROPERTIES = "validacion";
    
    public static boolean up(String infileAbsolutePath, String outfileAbsolutePath, String reason) {

        String fileKey = Textos.getTexto(PROPERTIES, "keystore.file");
        String fileKeyPassword = Textos.getTexto(PROPERTIES, "keystore.password");
        String alias = Textos.getTexto(PROPERTIES,"keystore.private.alias");
        String privatepassword = Textos.getTexto(PROPERTIES,"keystore.private.password");
        try {
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                ks.load(new FileInputStream(fileKey), fileKeyPassword.toCharArray());
                PrivateKey key = (PrivateKey) ks.getKey(alias, privatepassword.toCharArray());

                Certificate[] chain = ks.getCertificateChain(alias);
                PdfReader reader = new PdfReader(infileAbsolutePath);
                FileOutputStream fout = new FileOutputStream(outfileAbsolutePath);
                PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
                PdfSignatureAppearance sap = stp.getSignatureAppearance();
                sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
                sap.setReason(reason);
                sap.setLocation("Spain");
                sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
                stp.close();
            
        } catch (Exception key) {
            System.out.println("Error " + key);
            return false;
        }

        return true;
    }

        public static InputStream upI(InputStream infileAbsolutePath, String outfileAbsolutePath, String reason) {

        InputStream output = null;
        String fileKey = Textos.getTexto(PROPERTIES, "keystore.file");
        String fileKeyPassword = Textos.getTexto(PROPERTIES, "keystore.password");
        String alias = Textos.getTexto(PROPERTIES,"keystore.private.alias");
        String privatepassword = Textos.getTexto(PROPERTIES,"keystore.private.password");
        try {
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                ks.load(new FileInputStream(fileKey), fileKeyPassword.toCharArray());
                PrivateKey key = (PrivateKey) ks.getKey(alias, privatepassword.toCharArray());

                Certificate[] chain = ks.getCertificateChain(alias);
                PdfReader reader = new PdfReader(infileAbsolutePath);

                FileOutputStream fout = new FileOutputStream(outfileAbsolutePath);

                PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
                PdfSignatureAppearance sap = stp.getSignatureAppearance();
                sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
                sap.setReason(reason);
                sap.setLocation("Spain");
                sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
                stp.close();

        } catch (Exception key) {
            System.out.println("Error " + key);
        }

        return output;
    }
}
