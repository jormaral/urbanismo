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
package es.mitc.redes.urbanismoenred.consola.util.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="plan")
public class PlanHelper implements IJsonHelper {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.util.IJsonHelper#marshal(es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal, es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal, java.lang.Object, org.codehaus.jackson.map.ObjectMapper)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		
		ResourceBundle traductor = PropertyResourceBundle.getBundle("es.mitc.redes.urbanismoenred.consola.util.Traducciones"
				, new Locale(idioma,"ES"));
		
		Plan plan = servicioPlaneamiento.get(Plan.class, id);
		Map<String, Object> data = new HashMap<String, Object>();
        //GENERO LA LISTA DE COLUMNAS (EN ESTE CASO HABRA UNA SOLO)
		List<Object> columnlist = new ArrayList<Object>();
	
	    Map<String, Object> planJson = new HashMap<String, Object>();
	
	    Instrumentoplan instrumento = servicioDiccionario.getInstrumento(plan.getIden());
	    String nombreInstrumento = "-";

	    if (instrumento != null) {
	    	planJson.put("idInstrumento", instrumento.getIden());
	    	nombreInstrumento = servicioDiccionario.getTraduccion(Instrumentoplan.class, instrumento.getIden(), idioma);
	    }
	
	    planJson.put("iden", plan.getIden());
	    planJson.put("nombre", plan.getNombre());
	    planJson.put("codigo", plan.getCodigo());
		planJson.put("texto", plan.getTexto());
		planJson.put("orden", plan.getOrden());
	        
		planJson.put("instrumento", nombreInstrumento);
		
	    //NOMBRE DEL PLAN PADRE
	    String nomPlanPadre = "-";
	    if (plan.getPlanByIdpadre() != null) {
	    	planJson.put("idPadre", plan.getPlanByIdpadre().getIden());
	    	nomPlanPadre = plan.getPlanByIdpadre().getNombre();
	    }
	   	planJson.put("planPadre", nomPlanPadre);
	   	
	    //NOMBRE DEL PLAN PADRE
	    String nomPlanBase = "-";
	    if (plan.getPlanByIdplanbase() != null) {
	    	planJson.put("idBase", plan.getPlanByIdplanbase().getIden());
	    	nomPlanBase = plan.getPlanByIdplanbase().getNombre();
	    }
		planJson.put("planBase", nomPlanBase);
		
		planJson.put("idAmbito", plan.getIdambito());
		planJson.put("ambito", servicioDiccionario.getTraduccion(Ambito.class, plan.getIdambito(), idioma));
		
		if (plan.getBsuspendido()) {
			planJson.put("suspendida", traductor.getString("si"));
        } else {
        	planJson.put("suspendida", traductor.getString("no"));
        }
	        
		columnlist.add(planJson);
	        
		data.put("pages", 1);
		data.put("total", columnlist.size());
		data.put("data", columnlist);
		
		return data;
	}

}
