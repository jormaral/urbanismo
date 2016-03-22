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
package es.mitc.redes.urbanismoenred.consola.planeamiento.acciones;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioBusquedaLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "SEARCH_ON_TREE")
public class Buscar implements IAccion {
	
	private class Arbol {
		private Nodo raiz = new Nodo(0, null, false);
		
		public Arbol() {
		}
		
		public void addNodos(List<Object[]> ruta) {
			Nodo nodoActual = raiz;
			for (Object[] nodo : ruta) {
				if(nodoActual.getHijo((Integer)nodo[0]) == null) {
					nodoActual.addHijo(new Nodo((Integer)nodo[0],nodo[1], (Boolean)nodo[2]));
				} else {
					if ((Boolean)nodo[2]) {
						nodoActual.getHijo((Integer)nodo[0]).setResultado((Boolean)nodo[2]);
					}
				}
				nodoActual = nodoActual.getHijo((Integer)nodo[0]);
			}
		}

		public Nodo getRaiz() {
			return raiz;
		}
	}
	
	private class Nodo {
		private Map<Integer, Nodo> hijos = new HashMap<Integer,Nodo>();
		private Integer id;
		private Object objeto;
		private Boolean resultado;
		
		public Nodo(Integer id, Object objeto, Boolean resultado) {
			this.id = id;
			this.objeto = objeto;
			this.resultado = resultado;
		}
		
		public void addHijo(Nodo hijo) {
			hijos.put(hijo.getId(), hijo);
		}
		
		public Nodo getHijo(Integer id) {
			return hijos.get(id);
		}
		
		public Nodo[] getHijos() {
			return hijos.values().toArray(new Nodo[0]);
		}
		
		public Integer getId() {
			return id;
		}

		public Object getObjeto() {
			return objeto;
		}

		/**
		 * @return the resultado
		 */
		public Boolean getResultado() {
			return resultado;
		}

		/**
		 * @param resultado the resultado to set
		 */
		public void setResultado(Boolean resultado) {
			this.resultado = resultado;
		}
	}

	private static Map<String, Class<?>> correspondencias;
	private static final String DETERMINACION_APARTADO = "buscar.determinacion.apartado";
	private static final String DETERMINACION_CODIGO = "buscar.determinacion.codigo";
	private static final String DETERMINACION_CODIGOPLAN = "buscar.determinacion.plan.codigo";
	private static final String DETERMINACION_NOMBRE = "buscar.determinacion.nombre";
	private static final String DETERMINACION_RAIZ = "buscar.determinacion.raiz";
	private static final String DETERMINACION_TIPO = "buscar.determinacion.tipo";
	private static final String ENTIDAD_CLAVE = "buscar.entidad.clave";
	private static final String ENTIDAD_CODIGO = "buscar.entidad.codigo";
	private static final String ENTIDAD_CODIGOPLAN = "buscar.entidad.plan.codigo";
	private static final String ENTIDAD_NOMBRE = "buscar.entidad.nombre";
	private static final String ENTIDAD_RAIZ = "buscar.entidad.raiz";
	private static final String ENTIDAD_TIPO = "buscar.entidad.tipo";
	private static Map<Class<?>, String> inversas;
	private static final String PLAN_CODIGO = "buscar.plan.codigo";
	private static final String PLAN_DESDE = "buscar.plan.desde";
	private static final String PLAN_HASTA = "buscar.plan.hasta";
	private static final String PLAN_NOMBRE = "buscar.plan.nombre";
	private static final String PLAN_RAIZ = "buscar.plan.raiz";
	private static final String PLAN_TIPO = "buscar.plan.tipo";
	
	static {
		correspondencias = new HashMap<String, Class<?>>();
		correspondencias.put("determinacion", Determinacion.class);
		correspondencias.put("entidad", Entidad.class);
		correspondencias.put("tramite", Tramite.class);
		correspondencias.put("plan", Plan.class);
		correspondencias.put("ambito", Ambito.class);
		correspondencias.put("instrumentoplan", Instrumentoplan.class);
		
		inversas = new HashMap<Class<?>,String>();
		inversas.put(Determinacion.class, "determinacion");
		inversas.put(Entidad.class, "entidad");
		inversas.put(Tramite.class, "tramite");
		inversas.put(Plan.class, "plan");
		inversas.put(Ambito.class, "ambito");
		inversas.put(Instrumentoplan.class, "instrumentoplan");
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@EJB
	private ServicioBusquedaLocal servicioBusqueda;
	
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/**
	 * 
	 */
	public Buscar() {
	}

	/**
	 * 
	 * @param determinaciones
	 * @param idRaiz
	 * @param claseRaiz
	 * @return
	 */
	private Map<String, Object> crearArbol(Determinacion[] determinaciones, Integer idRaiz, Class<?> claseRaiz) {
		
		Map<String, Object> datos = new HashMap<String, Object>();
		
		List<Object[]> ruta;
		Arbol arbol = new Arbol();
		
		for (Determinacion determinacion : determinaciones) {
			ruta = new ArrayList<Object[]>();
			crearRuta(determinacion, idRaiz, claseRaiz, true, ruta);
			arbol.addNodos(ruta);
		}
		
		datos.put("resultado", generarMapeo(arbol, Determinacion.class.equals(claseRaiz)));
		datos.put("total", determinaciones.length);
		
		return datos;
	}

	/**
	 * 
	 * @param entidades
	 * @param idRaiz
	 * @param claseRaiz
	 * @return
	 */
	private Map<String, Object> crearArbol(Entidad[] entidades, Integer idRaiz, Class<?> claseRaiz) {
		Map<String, Object> datos = new HashMap<String, Object>();
		List<Object[]> ruta;
		Arbol arbol = new Arbol();
		
		for (Entidad entidad : entidades) {
			ruta = new ArrayList<Object[]>();
			crearRuta(entidad, idRaiz, claseRaiz, true, ruta);
			arbol.addNodos(ruta);
		}
		
		datos.put("resultado", generarMapeo(arbol, Entidad.class.equals(claseRaiz)));
		datos.put("total", entidades.length);
		
		return datos;
	}

	/**
	 * 
	 * @param planes
	 * @param idRaiz
	 * @param claseRaiz
	 * @return
	 */
	private Map<String, Object> crearArbol(Plan[] planes, Integer idRaiz,
			Class<?> claseRaiz) {
		Map<String, Object> datos = new HashMap<String, Object>();
		List<Object[]> ruta;
		Arbol arbol = new Arbol();
		
		for (Plan plan : planes) {
			ruta = new ArrayList<Object[]>();
			crearRuta(plan, idRaiz, claseRaiz, true, ruta);
			arbol.addNodos(ruta);
		}
		
		datos.put("resultado", generarMapeo(arbol, claseRaiz != null));
		datos.put("total", planes.length);
		
		return datos;
	}

	/**
	 * 
	 * @param determinacion
	 * @param idRaiz
	 * @param claseRaiz
	 * @param nodos
	 */
	private void crearRuta(Determinacion determinacion, Integer idRaiz,
			Class<?> claseRaiz, boolean resultado, List<Object[]> nodos) {
		nodos.add(0,new Object[]{determinacion.getIden(),determinacion, resultado});
		// Si tiene raíz de la búsqueda compruebo si el nodo coincide con la raíz.
		if (idRaiz != null && claseRaiz.equals(Determinacion.class) ) {
			if (!idRaiz.equals(determinacion.getIden()) && determinacion.getDeterminacionByIdpadre() != null) {
				crearRuta(determinacion.getDeterminacionByIdpadre(),idRaiz, claseRaiz, false, nodos);
			} 
		} else {
			if (determinacion.getDeterminacionByIdpadre() != null) {
				crearRuta(determinacion.getDeterminacionByIdpadre(),idRaiz, claseRaiz, false, nodos);
			} 
		}
	}

	/**
	 * 
	 * @param entidad
	 * @param idRaiz
	 * @param claseRaiz
	 * @param nodos
	 */
	private void crearRuta(Entidad entidad, Integer idRaiz, Class<?> claseRaiz, boolean resultado, List<Object[]> nodos) {
		nodos.add(0,new Object[]{entidad.getIden(),entidad, resultado});
		// Si tiene raíz de la búsqueda compruebo si el nodo coincide con la raíz.
		if (idRaiz != null && claseRaiz.equals(Entidad.class) ) {
			if (!idRaiz.equals(entidad.getIden()) && entidad.getEntidadByIdpadre() != null) {
				crearRuta(entidad.getEntidadByIdpadre(),idRaiz,claseRaiz, false, nodos);
			} 
		} else {
			if (entidad.getEntidadByIdpadre() != null) {
				crearRuta(entidad.getEntidadByIdpadre(),idRaiz,claseRaiz, false, nodos);
			} 
		}
	}

	/**
	 * 
	 * @param plan
	 * @param idRaiz
	 * @param claseRaiz
	 * @param nodos
	 */
	private void crearRuta(Plan plan, Integer idRaiz, Class<?> claseRaiz, 
			boolean resultado, List<Object[]> nodos) {
		nodos.add(0,new Object[]{plan.getIden(),plan, resultado});
		if (idRaiz != null && claseRaiz.equals(Plan.class) ) {
			if (!idRaiz.equals(plan.getIden()) && plan.getPlanByIdpadre() != null) {
				Instrumentoplan instrumento = servicioDiccionario.getInstrumento(plan.getIden());
				nodos.add(0,new Object[]{instrumento.getIden(),instrumento, false});
				crearRuta(plan.getPlanByIdpadre(),idRaiz, claseRaiz, false,
						nodos);
			} 
		} else {
			if (plan.getPlanByIdpadre() != null) {
				Instrumentoplan instrumento = servicioDiccionario.getInstrumento(plan.getIden());
				nodos.add(0,new Object[]{instrumento.getIden(),instrumento, false});
				crearRuta(plan.getPlanByIdpadre(),idRaiz, claseRaiz, false,
						nodos);
			} else {
				nodos.add(0,new Object[]{plan.getIdambito(), new Ambito(), false});
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ObjectMapper om = new ObjectMapper();
		
		String tipoBusqueda = request.getParameter("tipoBusqueda");
		String idRaiz = request.getParameter("idRaiz");
		String tipoRaiz = request.getParameter("tipoRaiz");
		String modalidad = request.getParameter("tipo");
		
		if (modalidad == null || modalidad.isEmpty()) {
			modalidad = ModalidadPlanes.TODOS.getCodigo();
		}
		
		if (tipoBusqueda != null) {
			try {
				if ("planes".equals(tipoBusqueda)) {
					
					String desde = request.getParameter("desde");
					String hasta = request.getParameter("hasta");
					
					request.getSession().setAttribute(PLAN_DESDE, desde);
					request.getSession().setAttribute(PLAN_HASTA, hasta);
					request.getSession().setAttribute(PLAN_NOMBRE, request.getParameter("nombrePlan"));
					request.getSession().setAttribute(PLAN_CODIGO, request.getParameter("codigoPlan"));
					request.getSession().setAttribute(PLAN_RAIZ, idRaiz);
					request.getSession().setAttribute(PLAN_TIPO, modalidad);
					
					Map<String, Object> resultado = crearArbol(servicioBusqueda.buscarPlanes(idRaiz != null && !idRaiz.isEmpty()?Integer.parseInt(idRaiz):null, 
								tipoRaiz != null && !tipoRaiz.isEmpty()? correspondencias.get(tipoRaiz):null, 
								request.getParameter("nombrePlan"), 
								request.getParameter("codigoPlan"), 
								desde != null && !desde.isEmpty()? sdf.parse(desde):null, 
								hasta != null && !hasta.isEmpty()? sdf.parse(hasta):null,
								ModalidadPlanes.valueOf(modalidad)),
							idRaiz != null && !idRaiz.isEmpty()?Integer.parseInt(idRaiz):null, 
							tipoRaiz != null && !tipoRaiz.isEmpty()? correspondencias.get(tipoRaiz):null);
					
					response.setContentType("application-json;charset=UTF-8");
					
					om.writeValue(response.getWriter(), resultado);
				} else if ("determinaciones".equals(tipoBusqueda)) {
					request.getSession().setAttribute(DETERMINACION_NOMBRE, request.getParameter("nombreDeterminacion"));
					request.getSession().setAttribute(DETERMINACION_CODIGO, request.getParameter("codigoDeterminacion"));
					request.getSession().setAttribute(DETERMINACION_APARTADO, request.getParameter("apartadoDeterminacion"));
					request.getSession().setAttribute(DETERMINACION_CODIGOPLAN, request.getParameter("codigoPlan"));
					request.getSession().setAttribute(DETERMINACION_RAIZ, idRaiz);
					request.getSession().setAttribute(DETERMINACION_TIPO, modalidad);
					
					Map<String, Object> resultado = crearArbol(servicioBusqueda.buscarDeterminaciones(idRaiz != null && !idRaiz.isEmpty()?Integer.parseInt(idRaiz):null, 
								tipoRaiz != null && !tipoRaiz.isEmpty()? correspondencias.get(tipoRaiz):null, 
								request.getParameter("nombreDeterminacion"), 
								request.getParameter("codigoDeterminacion"),  
								request.getParameter("apartadoDeterminacion"),
								ModalidadPlanes.valueOf(modalidad)),
							idRaiz != null && !idRaiz.isEmpty()?Integer.parseInt(idRaiz):null, 
							tipoRaiz != null && !tipoRaiz.isEmpty()? correspondencias.get(tipoRaiz):null);
					response.setContentType("application-json;charset=UTF-8");
					om.writeValue(response.getWriter(), resultado);
				} else if ("entidades".equals(tipoBusqueda)) {
					request.getSession().setAttribute(ENTIDAD_NOMBRE, request.getParameter("nombreEntidad"));
					request.getSession().setAttribute(ENTIDAD_CODIGO, request.getParameter("codigoEntidad"));
					request.getSession().setAttribute(ENTIDAD_CLAVE, request.getParameter("claveEntidad"));
					request.getSession().setAttribute(ENTIDAD_CODIGOPLAN, request.getParameter("codigoPlan"));
					request.getSession().setAttribute(ENTIDAD_RAIZ, idRaiz);
					request.getSession().setAttribute(ENTIDAD_TIPO, modalidad);
					
					Map<String, Object> resultado = crearArbol(servicioBusqueda.buscarEntidades(idRaiz != null && !idRaiz.isEmpty()?Integer.parseInt(idRaiz):null, 
								tipoRaiz != null && !tipoRaiz.isEmpty()? correspondencias.get(tipoRaiz):null, 
								request.getParameter("nombreEntidad"), 
								request.getParameter("codigoEntidad"),  
								request.getParameter("claveEntidad"),
								ModalidadPlanes.valueOf(modalidad)),
							idRaiz != null && !idRaiz.isEmpty()?Integer.parseInt(idRaiz):null, 
							tipoRaiz != null && !tipoRaiz.isEmpty()? correspondencias.get(tipoRaiz):null);
					response.setContentType("application-json;charset=UTF-8");
					om.writeValue(response.getWriter(), resultado);
				} else {
					response.getWriter().print("Consulta no soportada.");
				}
			} catch (ParseException pe) {
				response.getWriter().print("Fecha incorrecta.");
			} catch (NumberFormatException nfe) {
				response.getWriter().print("Identificador incorrecto.");
			}
		} else {
			response.getWriter().print("Parámetros incompletos");
		}
		
	}
	
	/**
	 * 
	 * @param arbol
	 * @return
	 */
	private List<Object> generarMapeo(Arbol arbol, boolean eliminarRaiz) {
		List<Object> datos = new ArrayList<Object>();
		Nodo raiz = arbol.getRaiz();
		if (eliminarRaiz && raiz.getHijos().length >0) {
			raiz = raiz.getHijos()[0];
		}
		for(Nodo nodo :raiz.getHijos()) {
			datos.add(recorrerNodo(nodo));
		}
		return datos;
	}
	
	/**
	 * 
	 * @param nodo
	 * @return
	 */
	private Map<String, Object> recorrerNodo(Nodo nodo) {
		Map<String,Object> nodoJson = new HashMap<String, Object>();
		nodoJson.put("id", nodo.getId());
		// A veces no se devuelve la clase, sino el wrapper creado por Javassist
		String clase = inversas.get(nodo.getObjeto().getClass());
		if (clase == null) {
			clase = inversas.get(nodo.getObjeto().getClass().getSuperclass());
		}
		nodoJson.put("tipo", clase);
		nodoJson.put("resultado", nodo.getResultado());
		
		List<Object> hijos = new ArrayList<Object>();
		for (Nodo hijo : nodo.getHijos()) {
			hijos.add(recorrerNodo(hijo));
		}
		if (hijos.size() > 0) {
			nodoJson.put("siguiente", hijos);
		}
		return nodoJson;
	}

}
