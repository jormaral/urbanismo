/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/

package es.mitc.redes.urbanismoenred.servicios.validacion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorDocumentosLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorTramitesLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ExcepcionSeguridad;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioFipLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless
public class ServicioValidacionIntegridadBean implements ServicioValidacionIntegridadRemote, ServicioValidacionIntegridadLocal {

	private static final Logger log = Logger.getLogger(ServicioValidacionIntegridadBean.class);
    private static final int ID_REFUNDIDO_AUTOMATICO = 11;
    private static final int CODIGO_VALIDACION_USUARIO = 1;
    private static final int CODIGO_VALIDACION_INTEGRIDAD = 0;

    // --- Variables de conexion a otros EJB --- //
    @EJB
    private GestionIntroduccionFIPenSistemaLocal gestorFIP;
    @EJB
    private GestorDocumentosLocal gestorDocumentos;
    @EJB
    private GestorTramitesLocal gestorTramites;
    @EJB
    private ServicioFipLocal svFip1;
    @EJB
    private ServicioUsuariosLocal svUsuario;
    @EJB
    private ServicioResultadosValidacionLocal srvResultados;

    /**
     * Comprueba que los documentos especificados en el FIP se encuentran en el
     * sistema de archivos.
     * 
     * @param fip FIP que define los documentos a comprobar.
     * @return Verdadero si los documentos son válidos y falso en caso contrario.
     */
    private void validarDocumentosFisicos(String codigoTramite, String ruta, List<String> errores) {
        log.info("Validando documentos del FIP.");

        File fich = null;
        
        Documento[] documentos = gestorDocumentos.getDocumentos(codigoTramite);
        
        for (int i = 0; i < documentos.length;i++) {
        	log.debug("Comprobando documento " + i + " de " + documentos.length);
        	
        	// Si tiene documento original significa que proviene de refundido 
        	// y por tanto el archivo físico está en el documento referenciado
        	if (documentos[i].getDocumento() == null) {
        		fich = new File(ruta + java.io.File.separator + documentos[i].getArchivo().replace("\\", java.io.File.separator));

                if (!fich.exists()) {
                    log.error("No existe documento: " + fich.getPath());
                    errores.add("No existe documento: " + documentos[i].getNombre());
                }
        	}
        }
        
        Documentoshp[] hojas = gestorDocumentos.getHojas(codigoTramite);
        
        for (int i = 0; i < hojas.length;i++) {
        	log.debug("Comprobando hoja " + i + " de " + hojas.length);
        	fich = new File(ruta + java.io.File.separator 
        				+ hojas[i].getDocumento()
        					.getArchivo()
        					.replace("\\", java.io.File.separator) 
        				+ java.io.File.separator + hojas[i].getHoja());
        	
            if (!fich.exists()) {
                log.error("No existe hoja: " + fich.getPath());
                errores.add("No existe hoja: " + hojas[i].getDocumento()
    					.getArchivo()
    					.replace("\\", java.io.File.separator) 
    				+ java.io.File.separator + hojas[i].getHoja());
            }
        }
    }

   /*
    * (non-Javadoc)
    * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionIntegridadRemote#validar(long, java.lang.String)
    */
	@Override
	public boolean validar(
			String idFip, String nombreUsuario)
			throws ExcepcionValidacion {
		log.info("Comenzando proceso de validación de integridad.");
		
		List<String> errores = new ArrayList<String>();
		try {
			//Busco el ambito del tramite
	        Tramite tram = gestorTramites.getTramite(idFip);

	        // Realiza la validacion para comprobar si un fip con el mismo codigo existe ya en la BD
	        if (tram != null) {
	        	
	        	if (validarUsuario(nombreUsuario, tram, idFip)) {
	        		// Realiza la validacion de que el FIP tipo 1 no haya caducado
	                verificarVigenciaFIP1(idFip, errores);
	                
	                if (tram.getIdtipotramite() != ID_REFUNDIDO_AUTOMATICO) {
	                	String xmlFip = gestorFIP.getUltimaVersionFIP(idFip);
	                    // Realiza la validacion de que los documentos incluidos en el fip.xml se encuentren fisicamente en el sistema
	                	File file = new File(xmlFip);
	                	validarDocumentosFisicos(idFip, file.getParent(),errores);
	                }
	        	} else {
	        		errores.add("No se ha podido realizar la validación de integridad por el usuario " + nombreUsuario);
	        	}
	        } else {
	        	log.debug("El código FIP no está en RPM");
	        	errores.add("No se ha encontrado un trámite con código " + idFip);
	        }
	        
	        try {
	    		srvResultados.registrarResultado(idFip,
	        		CODIGO_VALIDACION_INTEGRIDAD, 
	        		errores.size() == 0, 
	        		errores
	        		);
	    	} catch (ExcepcionPersistencia ep) {
	    		log.warn("No se ha podido guardar el resultado. Causa: " + ep.getMessage());
	    	}
		} catch (RedesException e) {
			throw new ExcepcionValidacion("No se pudo obtener el FIP. Causa: " + e.getMessage());
		}
		
		return errores.size()==0;
	}
	
	/**
	 * 
	 * @param nombreUsuario
	 * @param tramite
	 * @param idFip
	 * @return
	 */
	private boolean validarUsuario(String nombreUsuario, Tramite tramite, String idFip) {
		//Busco los ambitos para los que el usuario tiene permisos.
        Usuario usuario = svUsuario.getUsuario(nombreUsuario);
        List<String> errores = new ArrayList<String>();
        if (usuario != null) {
        	// Se comprueba si el usuario tiene permisos para validar el FIP
        	
            if (!usuario.puedeValidar(tramite.getPlan().getIdambito())) {
            	errores.add("Usuario: " + usuario.getNombre() + " no tiene permisos para validar.");
            }
        } else {
        	errores.add("No existe usuario con nombre " + nombreUsuario);
        }
        
        try {
    		srvResultados.registrarResultado(idFip,
    			CODIGO_VALIDACION_USUARIO, 
        		errores.size()==0, 
        		errores
        		);
    	} catch (ExcepcionPersistencia ep) {
    		log.warn("No se ha podido guardar el resultado. Causa: " + ep.getMessage());
    	}
        
        return errores.size() == 0;
	}
	
	/**
	 * 
	 * @param fip
	 * @param errores 
	 * @return
	 */
	private boolean verificarVigenciaFIP1(String  codigoTramite, List<String> errores) throws ExcepcionValidacion {
          
		try {
			if (svFip1.esObsoleto(codigoTramite)) {
				errores.add("El FIP "+codigoTramite+" es obsoleto");
				return false;
	        }
			return true;
		} catch (ExcepcionSeguridad e) {
			 errores.add(e.getMessage());
			 return false;
		}
    }
}


