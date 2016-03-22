/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.data.rpm.diccionario;
// Generated 08-jul-2009 12:36:15 by Hibernate Tools 3.2.1.GA

import es.mitc.redes.urbanismoenred.servicios.comunes.EncriptacionCodigoTramite;
import es.mitc.redes.urbanismoenred.utils.consola.OgridColumnModel;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Centroproduccion generated by hbm2java
 */
public class Centroproduccion implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6585370786706291564L;
	private int iden;
    private Literal literal;
    private String codigo;
    private String passwordmd5;
    private String mail;

    public Centroproduccion() {
    }

    public Centroproduccion(int iden, String codigo) {
        this.iden = iden;
        this.codigo = codigo;
    }

    public Centroproduccion(int iden, Literal literal, String codigo) {
        this.iden = iden;
        this.literal = literal;
        this.codigo = codigo;
    }

    public int getIden() {
        return this.iden;
    }

    public void setIden(int iden) {
        this.iden = iden;
    }

    public Literal getLiteral() {
        return this.literal;
    }

    public void setLiteral(Literal literal) {
        this.literal = literal;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPasswordmd5() {
        return this.passwordmd5;
    }

    public void setPasswordmd5(String passwordmd5) {
        this.passwordmd5 = passwordmd5;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public static Map<String, Object> getColumnModel() {
        Map<String, Object> res = new HashMap<String, Object>();
        List<Map<String, Object>> lcms = new ArrayList<Map<String, Object>>();

        lcms.add((new OgridColumnModel("Id", "iden", "number", 126)).toJSON());
        lcms.add((new OgridColumnModel("Nombre", "nombre", "string", false, 300)).toJSON());
        lcms.add((new OgridColumnModel("Email", "mail", "string", 220)).toJSON());
        res.put("cm", lcms);
        return res;
    }

    public Map<String, Object> toJSON(String idioma) {
        Map<String, Object> res = new HashMap<String, Object>();

        res.put("iden", this.getIden());
        res.put("nombre", this.getLiteral().getTexto(idioma));
        res.put("mail", this.getMail());

        return res;
    }

    public String fromJSON(Map<String, Object> data, String idioma) throws IOException, NoSuchAlgorithmException {
        String result = "Correcto";
        Set<String> ks = data.keySet();
        if (!ks.contains("iden") || !ks.contains("nombre") || !ks.contains("mail") || !ks.contains("password")) {
            throw new IOException("No se ha podido cargar el objeto a partir de la información JSON. Datos incompletos");
        }

        Literal lit = this.literal;
        if (lit == null) {
            lit = new Literal();
            this.setLiteral(lit);
        }
        //Comprobaciones en el servidor de los datos recibidos
        //Nombre no vacio
        if(!data.get("nombre").toString().equals("")){
            lit.addTraduccion(data.get("nombre").toString(), idioma);
        }else{
            result="El nombre llega vacio al servidor";
        }

        this.setCodigo(data.get("codigo").toString());

        //Comprobamos formato del mail
        Pattern pat = null;
        Matcher mat = null;
        pat = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[a-zA-Z0-9_]+(\\.[a-zA-Z]+)+");
        mat = pat.matcher(data.get("mail").toString());
        if(mat.find()){
            this.setMail(data.get("mail").toString());
        }else{
            result="El mail que llega al servidor no es correcto";
        }
        // Comprobamos que la password no llegue vacia
        if (!data.get("password").toString().equals("")){
            this.passwordmd5 = EncriptacionCodigoTramite.getEncoded(data.get("password").toString());
        }else{
            result="La password ha llegado vacia al servidor";
        }

        return result;
    }
}


