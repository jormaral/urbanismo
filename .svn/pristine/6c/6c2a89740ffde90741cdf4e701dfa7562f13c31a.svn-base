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
package es.mitc.redes.urbanismoenred.servicios.planeamiento;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambitoshp;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Boletin;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Caracterdeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Grupodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Literal;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Naturaleza;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Operacioncaracter;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Organo;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Traduccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.servicios.comunes.EncriptacionCodigoTramite;

/**
 * Session Bean implementation class GestorDiccionariosBean
 */
@Stateless(name = "ServicioDiccionarios")
public class ServicioDiccionariosBean implements ServicioDiccionariosLocal {
	
	private static final Logger log = Logger.getLogger(ServicioDiccionariosBean.class);
	
	@Resource(name = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "java:/queue/CambiosBD")
	private static Queue queue;
	
	@PersistenceContext(unitName="rpmv2")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public ServicioDiccionariosBean() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#borrar(java.lang.Class, java.lang.Object)
     */
    @Override
	public void borrar(Class<?> clase, Object identificador)
			throws ExcepcionDiccionario {
		Object object = em.find(clase, identificador);
		if (object != null) {
			em.remove(object);
			
			notificarModificacion();
		} else { 
			throw new ExcepcionDiccionario("No se ha encontrado el elemento con identificador " + identificador);
		}
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearBoletin(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
	public Boletin crearBoletin(String nombre, String comentario, String idioma)
			throws ExcepcionDiccionario {
    	if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de boletín");
		Boletin boletin = new Boletin();
		Literal literal = new Literal();
		literal.setComentario(comentario);
		boletin.setLiteral(literal);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setTexto(nombre);
		traduccion.setLiteral(literal);
		em.persist(literal);
		em.persist(traduccion);
		em.persist(boletin);
		
		notificarModificacion();
		
		return boletin;
	}

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearCaracterDeterminacion(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
     */
	@Override
	public Caracterdeterminacion crearCaracterDeterminacion(String nombre,
			Integer minimoAplicaciones, Integer maximoAplicaciones,
			String comentario, String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de carácter");
		Caracterdeterminacion cd = new Caracterdeterminacion();
		cd.setNmaxaplicaciones(maximoAplicaciones);
		cd.setNminaplicaciones(minimoAplicaciones);
		Literal literal = new Literal();
		literal.setComentario(comentario);
		cd.setLiteral(literal);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(cd);
		
		notificarModificacion();
		
		return cd;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearCentroProduccion(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Centroproduccion crearCentroProduccion(String nombre, String correo,
			String password, String comentario, String idioma)
			throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre");
		
		Centroproduccion cp = new Centroproduccion();
		
		if (correo != null && !correo.isEmpty()) {
			if (correo.matches("[a-zA-Z0-9][a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[a-zA-Z0-9_]+(\\.[a-zA-Z]+)+")) {
				cp.setMail(correo);
			} else {
				throw new ExcepcionDiccionario("Dirección de correo electrónico inválida.");
			}
		}
		cp.setPasswordmd5(EncriptacionCodigoTramite.getEncoded(password));
		Literal literal = new Literal();
		literal.setComentario(comentario);
		cp.setLiteral(literal);
		
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(cp);
		em.flush();

		cp.setCodigo(new DecimalFormat("00000").format(cp.getIden()));
		
		return cp;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearGrupoDocumento(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Grupodocumento crearGrupoDocumento(String nombre, String comentario,
			String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de grupo");
		Grupodocumento grupo = new Grupodocumento();
		Literal literal = new Literal();
		literal.setComentario(comentario);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		grupo.setLiteral(literal);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(grupo);
		
		notificarModificacion();
		
		return grupo;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearInstrumentoPlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Instrumentoplan crearInstrumentoPlan(String nombre, String nemo,
			String comentario, String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nemo == null || nemo.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nemo de instumento");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de instrumento");
		Instrumentoplan ip = new Instrumentoplan();
		ip.setNemo(nemo);
		Literal literal = new Literal();
		literal.setComentario(comentario);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		ip.setLiteral(literal);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(ip);
		
		notificarModificacion();
		
		return ip;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearInstrumentoTipoOperacionPlan(int, int)
	 */
	@Override
	public Instrumentotipooperacionplan crearInstrumentoTipoOperacionPlan(int tipoOperacion,
			int instrumento) throws ExcepcionDiccionario {
		Tipooperacionplan operacion = em.find(Tipooperacionplan.class, tipoOperacion);
		if (operacion == null)
			throw new ExcepcionDiccionario("No se ha encontrado tipo de operacioón con identificador " + tipoOperacion);
		
		Instrumentoplan instrumentoPlan = em.find(Instrumentoplan.class, instrumento);
		
		if (instrumentoPlan == null)
			throw new ExcepcionDiccionario("No se ha encontrado instrumento con identificador " + instrumento);
		
		Instrumentotipooperacionplan itop = new Instrumentotipooperacionplan();
		itop.setInstrumentoplan(instrumentoPlan);
		itop.setTipooperacionplan(operacion);
		
		em.persist(itop);
		
		notificarModificacion();
		
		return itop;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearNaturaleza(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Naturaleza crearNaturaleza(String nombre, String comentario,
			String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de naturaleza");
		Naturaleza naturaleza = new Naturaleza();
		Literal literal = new Literal();
		literal.setComentario(comentario);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		naturaleza.setLiteral(literal);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(naturaleza);
		
		notificarModificacion();
		
		return naturaleza;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearOperacionCaracter(int, int, java.lang.Integer)
	 */
	@Override
	public Operacioncaracter crearOperacionCaracter(int caracterOperado,
			int caracterOperador, Integer tipoOperacion)
			throws ExcepcionDiccionario {
		Operacioncaracter oc = new Operacioncaracter();
		Caracterdeterminacion caracteroperado = em.find(Caracterdeterminacion.class, caracterOperado);
		if (caracteroperado == null)
			throw new ExcepcionDiccionario("No se ha encontrado caracter operado con identificador " + caracterOperado);
		oc.setCaracterdeterminacionByIdcaracteroperado(caracteroperado);
		Caracterdeterminacion caracteroperador = em.find(Caracterdeterminacion.class, caracterOperador);
		if (caracteroperador == null)
			throw new ExcepcionDiccionario("No se ha encontrado caracter operador con identificador " + caracterOperador);
		oc.setCaracterdeterminacionByIdcaracteroperador(caracteroperador);
		Tipooperaciondeterminacion tipooperacion = em.find(Tipooperaciondeterminacion.class, tipoOperacion);
		oc.setTipooperaciondeterminacion(tipooperacion);
		
		em.persist(oc);
		
		notificarModificacion();
		
		return oc;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearOrgano(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Organo crearOrgano(String nombre, String comentario, String idioma)
			throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de órgano");
		
		Organo organo = new Organo();
		Literal literal = new Literal();
		literal.setComentario(comentario);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		organo.setLiteral(literal);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(organo);
		
		notificarModificacion();
		
		return organo;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearTipodocumento(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Tipodocumento crearTipodocumento(String nombre, String comentario,
			String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de órgano");
		
		Tipodocumento tipo = new Tipodocumento();
		Literal literal = new Literal();
		literal.setComentario(comentario);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		tipo.setLiteral(literal);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(tipo);
		
		notificarModificacion();
		
		return tipo;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearTipoOperacionPlan(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Tipooperacionplan crearTipoOperacionPlan(String nombre,
			String comentario, String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de tipo de operación");
		
		Tipooperacionplan tipo = new Tipooperacionplan();
		Literal literal = new Literal();
		literal.setComentario(comentario);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		tipo.setLiteral(literal);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(tipo);
		
		notificarModificacion();
		
		return tipo;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#crearTipoTramite(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Tipotramite crearTipoTramite(String nombre, String comentario, String idioma)
			throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de trámite");
		Tipotramite tipoTramite = new Tipotramite();
		Literal literal = new Literal();
		literal.setComentario(comentario);
		Traduccion traduccion = new Traduccion();
		traduccion.setIdioma(idioma);
		traduccion.setLiteral(literal);
		traduccion.setTexto(nombre);
		tipoTramite.setLiteral(literal);
		
		em.persist(literal);
		em.persist(traduccion);
		em.persist(tipoTramite);
		
		notificarModificacion();
		
		return tipoTramite;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#get(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] get(Class<T> clase) {
		List<?> resultado = em.createNamedQuery(clase.getSimpleName()+".obtenerTodos").getResultList();	
		
		return resultado.toArray((T[])Array.newInstance(clase, 0));
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#get(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T get(Class<T> clase, Object id) {
		return em.find(clase, id);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#getInstrumento(int)
	 */
	@Override
	public Instrumentoplan getInstrumento(int idPlan) {
		Plan plan = em.find(Plan.class, idPlan);
		
		if (plan != null) {
			Set<Operacionplan> operaciones = plan.getOperacionplansForIdplanoperador();
			
			if (!operaciones.isEmpty()) {
				Instrumentotipooperacionplan ito = em.find(Instrumentotipooperacionplan.class, 
						operaciones.iterator().next().getIdinstrumentotipooperacion());
				return ito.getInstrumentoplan();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#getInstrumentoTipoOperacion(int, int)
	 */
	@Override
	public Instrumentotipooperacionplan getInstrumentoTipoOperacion(
			int idTipoOperacion, int idInstrumento) {
		return (Instrumentotipooperacionplan) em.createNamedQuery("Instrumentotipooperacionplan.buscarPorInstrumentoYOperacion")
				.setParameter("idTipoOperacion", idTipoOperacion)
				.setParameter("idInstrumento", idInstrumento).getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#getOperaciones(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Tipooperacionplan[] getOperaciones(int idInstrumentoPlan) {
		List<Instrumentotipooperacionplan> itops = em.createNamedQuery("Instrumentotipooperacionplan.buscarPorInstrumento")
				.setParameter("idInstrumento", idInstrumentoPlan)
				.getResultList();
		List<Tipooperacionplan> operaciones = new ArrayList<Tipooperacionplan>();
		for (Instrumentotipooperacionplan itop : itops) {
			if (!operaciones.contains(itop.getTipooperacionplan())) {
				operaciones.add(itop.getTipooperacionplan());
			}
		}
		return operaciones.toArray(new Tipooperacionplan[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorDiccionariosLocal#getPoligonosAmbito(int)
	 */
	@Override
	public String[] getPoligonosAmbito(int idAmbito) {
		Ambito ambito = em.find(Ambito.class, idAmbito);
		List<String> poligonos = new ArrayList<String>();
		if (ambito != null) {
			for (Ambitoshp shp : ambito.getAmbitoshp()) {
				if (shp.getGeom() != null)
					poligonos.add(shp.getGeom());
			}
		} 
		return poligonos.toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorDiccionariosLocal#getTraduccion(java.lang.Class, java.lang.Object, java.lang.String)
	 */
	@Override
	public String getTraduccion(Class<?> clase, Object identificador,
			String idioma) {
		Object instancia = em.find(clase, identificador);
		
		if (instancia != null) {
			try {
				Literal literal = (Literal)clase.getMethod("getLiteral", new Class<?>[0]).invoke(instancia,new Object[0]);
				for (Traduccion traduccion: literal.getTraduccions()) {
					if (traduccion.getIdioma().equals(idioma)) {
						return traduccion.getTexto();
					}
				}
			} catch (SecurityException e) {
				log.warn("No se pudo obtener traducción. Clase : " 
						+ clase.getName() + " Identificador: " + identificador 
						+ " Idioma: " + idioma + " Causa: " + e.getMessage());
			} catch (NoSuchMethodException e) {
				log.warn("No se pudo obtener traducción. Clase : " 
						+ clase.getName() + " Identificador: " + identificador 
						+ " Idioma: " + idioma + " Causa: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				log.warn("No se pudo obtener traducción. Clase : " 
						+ clase.getName() + " Identificador: " + identificador 
						+ " Idioma: " + idioma + " Causa: " + e.getMessage());
			} catch (IllegalAccessException e) {
				log.warn("No se pudo obtener traducción. Clase : " 
						+ clase.getName() + " Identificador: " + identificador 
						+ " Idioma: " + idioma + " Causa: " + e.getMessage());
			} catch (InvocationTargetException e) {
				log.warn("No se pudo obtener traducción. Clase : " 
						+ clase.getName() + " Identificador: " + identificador 
						+ " Idioma: " + idioma + " Causa: " + e.getMessage());
			}
		}
		
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#modificarCentroProduccion(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Centroproduccion modificarCentroProduccion(int identificador, String nombre,
			String codigo, String correo, String password, String comentario,
			String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre");
		if (codigo == null || codigo.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un código");
		
		Centroproduccion cp = em.find(Centroproduccion.class, identificador);
		if (cp != null) {
			cp.setCodigo(codigo);
			if (correo != null && !correo.isEmpty()) {
				if (correo.matches("[a-zA-Z0-9][a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[a-zA-Z0-9_]+(\\.[a-zA-Z]+)+")) {
					cp.setMail(correo);
				} else {
					throw new ExcepcionDiccionario("Dirección de correo electrónico inválida.");
				}
			}
			cp.setPasswordmd5(EncriptacionCodigoTramite.getEncoded(password));
			Literal literal = cp.getLiteral();
			literal.setComentario(comentario);
			boolean encontrado = false;
			for (Traduccion traduccion : literal.getTraduccions()) {
				if (traduccion.getIdioma().equals(idioma)) {
					traduccion.setTexto(nombre);
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				Traduccion traduccion = new Traduccion();
				traduccion.setIdioma(idioma);
				traduccion.setLiteral(literal);
				traduccion.setTexto(nombre);
				em.persist(traduccion);
			}
			
			return cp;
		} else {
			throw new ExcepcionDiccionario("No se ha encontrado centro de producción con identificador " + identificador);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal#modificarTipoTramite(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Tipotramite modificarTipoTramite(int identificador, String nombre,
			String comentario, String idioma) throws ExcepcionDiccionario {
		if (idioma == null || idioma.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un idioma");
		if (nombre == null || nombre.isEmpty())
			throw new ExcepcionDiccionario("Debe especificarse un nombre de trámite");
		
		
		
		Tipotramite tipoTramite = em.find(Tipotramite.class, identificador);
		if (tipoTramite != null) {
			Literal literal = tipoTramite.getLiteral();
			literal.setComentario(comentario);
			
			boolean encontrado = false;
			for (Traduccion traduccion : literal.getTraduccions()) {
				if (traduccion.getIdioma().equals(idioma)) {
					traduccion.setTexto(nombre);
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				Traduccion traduccion = new Traduccion();
				traduccion.setIdioma(idioma);
				traduccion.setLiteral(literal);
				traduccion.setTexto(nombre);
				em.persist(traduccion);
			}
			
			notificarModificacion();
			
			return tipoTramite;
		} else {
			throw new ExcepcionDiccionario("No se ha encontrado tipo de trámite con identificador " + identificador);
		}
	}
	
	/**
	 * 
	 */
	private void notificarModificacion() {
		try {
			Connection conexion = connectionFactory.createConnection();
			
			Session sesion = conexion.createSession(true, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer productor = sesion.createProducer(queue);
			
			MapMessage mensaje = sesion.createMapMessage();
			
			mensaje.setStringProperty("DatosModificados", "Diccionario");
			
			productor.send(mensaje);
			
			sesion.commit();
			
			sesion.close();
			conexion.close();
			
		} catch (JMSException e) {
			log.warn("No se ha podido notificar el cambio en el diccionario: " + e.getMessage());
		}
	}

}
