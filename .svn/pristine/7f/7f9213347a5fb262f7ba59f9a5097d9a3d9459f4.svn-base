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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * @author Arnaiz Consultores
 *
 */
public class ContextoRefundidoBasico implements ContextoRefundido{
	
	private static final String ARCHIVO_TRADUCCIONES = "es.mitc.redes.urbanismoenred.utils.recursos.Traducciones";
	
	private static final Logger log = Logger.getLogger(ContextoRefundidoBasico.class);
	
	private static final String DIRECTORIO_TRABAJO = "var";
	private File archivoLog;
	private boolean correcto = true;
	private boolean crearTramite = false;
	private FileWriter fw;
	private Estado estado = Estado.PENDIENTE;
	private int idAmbito;
	private Map<String, Object> parametros = new HashMap<String, Object>();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat logsdf=new SimpleDateFormat("(HH:mm:ss) ");
	private List<Integer> tramites = new ArrayList<Integer>();
	
	private PropertyChangeSupport soporteEventos;

	private Usuario usuario;
	private File fip;
	
	public ContextoRefundidoBasico(List<Integer> tramites, Usuario usuario, boolean crearTramite) {
		this.tramites.addAll(tramites);
		this.usuario = usuario;
		this.crearTramite = crearTramite;
		
		soporteEventos = new PropertyChangeSupport(this);
		
		setIdioma("es");
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#agregarListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void agregarListener(PropertyChangeListener listener) {
		soporteEventos.addPropertyChangeListener(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#agregarListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	@Override
	public void agregarListener(String propiedad,
			PropertyChangeListener listener) {
		soporteEventos.addPropertyChangeListener(propiedad, listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#eliminarListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void eliminarListener(PropertyChangeListener listener) {
		soporteEventos.removePropertyChangeListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#eliminarListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	@Override
	public void eliminarListener(String propiedad,
			PropertyChangeListener listener) {
		soporteEventos.removePropertyChangeListener(propiedad, listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#finalizarProceso()
	 */
	@Override
	public void finalizarProceso(){
		try {
			parametros.clear();
			fw.close();
		} catch (IOException e) {
			// Se ignora el error
		} finally { 
			if (correcto)
				setEstado(Estado.FINALIZADO);
			else
				setEstado(Estado.ERRONEO);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getArchivoLog()
	 */
	public File getArchivoLog() {
		return archivoLog;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getEstado()
	 */
	@Override
	public Estado getEstado() {
		return estado;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getFIP()
	 */
	@Override
	public File getFIP() {
		return fip;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getIdAmbito()
	 */
	@Override
	public int getIdAmbito() {
		return idAmbito;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getIdioma()
	 */
	@Override
	public String getIdioma() {
		
		if (parametros.containsKey(IDIOMA)) {
			return (String) parametros.get(IDIOMA);
		} else {
			return "es";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getParametro(java.lang.String)
	 */
	@Override
	public Object getParametro(String clave) {
		return parametros.get(clave);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getTramites()
	 */
	@Override
	public List<Integer> getTramites() {
		return tramites;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#getUsuario()
	 */
	@Override
	public Usuario getUsuario() {
		return usuario;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#iniciarProceso()
	 */
	@Override
	public void iniciarProceso() throws ExcepcionRefundido {
		String baseDirPath = System.getenv("REDES_PATH")+File.separator + DIRECTORIO_TRABAJO +File.separator+"refundido";
		setEstado(Estado.INICIADO);
		correcto = true;
		File dir = new File(baseDirPath);
		if (dir != null) {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			archivoLog = new File(dir,"Refundido_"+usuario.getNombre()+"_"+sdf.format(GregorianCalendar.getInstance().getTime())+".log");
			if (archivoLog != null) {
				try {
					fw = new FileWriter(archivoLog);
				} catch (IOException e) {
					setEstado(Estado.FINALIZADO);
					throw new ExcepcionRefundido(e.getMessage(), 2001);
				}
			} else {
				setEstado(Estado.FINALIZADO);
				throw new ExcepcionRefundido("No se ha podido crear archivo de log.", 2002);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#isCrearTramite()
	 */
	@Override
	public boolean isCrearTramite() {
		return crearTramite;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#log(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG, java.lang.String)
	 */
	@Override
	public void log(LOG tipo, String mensaje) {
		try {
			log.debug(mensaje);
			
			fw.write(logsdf.format(GregorianCalendar.getInstance().getTime()));
			
			if (!LOG.INFO.equals(tipo)) {
				if (LOG.ERROR.equals(tipo)) {
					correcto = false;
				}
				fw.write(tipo.toString());
				fw.write(": ");
			}
			fw.write(mensaje);
			fw.write("\r\n");
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#logTraducido(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void logTraducido(LOG tipo, String mensaje, Object... valores) {
		ResourceBundle traductor = (ResourceBundle) parametros.get(TRADUCTOR);
		log(tipo,String.format(traductor.getString(mensaje), valores));
		
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#putParametro(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putParametro(String clave, Object valor) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this,clave,parametros.get(clave),valor);
		parametros.put(clave, valor);
		soporteEventos.firePropertyChange(evt);
	}

	protected void setEstado(Estado estado) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this,ESTADO,this.estado,estado);
		this.estado = estado;
		soporteEventos.firePropertyChange(evt);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#setFIP(java.io.File)
	 */
	@Override
	public void setFIP(File fip) {
		this.fip = fip;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#setIdAmbito(int)
	 */
	@Override
	public void setIdAmbito(int idAmbito) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this, AMBITO, this.idAmbito, idAmbito);
		this.idAmbito = idAmbito;
		soporteEventos.firePropertyChange(evt);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido#setIdioma(java.lang.String)
	 */
	@Override
	public void setIdioma(String idioma) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this,IDIOMA,parametros.get(IDIOMA),idioma);
		parametros.put(IDIOMA, idioma);
		soporteEventos.firePropertyChange(evt);
		ResourceBundle traductor = PropertyResourceBundle.getBundle(ARCHIVO_TRADUCCIONES
				, new Locale(idioma,"ES"));
		putParametro(TRADUCTOR, traductor);
	}
}
