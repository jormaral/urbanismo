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
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="PlanOperaA")
public class PlanOperaHelper implements IJsonHelper {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.helpers.IJsonHelper#marshal(java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		
		Plan plan = servicioPlaneamiento.get(Plan.class, (Integer)id);
		
		Map<String, Object> data = new HashMap<String, Object>();
        //GENERO LA LISTA DE COLUMNAS (EN ESTE CASO HABRA UNA SOLO)
        List<Object> columnlist = new ArrayList<Object>();
        Map<String, Object> linea;
        Plan operado;
        String nombreInstrumento;
        
		for (Operacionplan operacionPlan : plan.getOperacionplansForIdplanoperador()) {
			//ESTE PLAN ES OPERADO POR...
			operado=operacionPlan.getPlanByIdplanoperado();
			
			//OBTENGO LOS NOMBRES DE LOS PLANES ENCONTRADOS
			linea = new HashMap<String, Object>();
            linea.put("iden", plan.getIden());
            linea.put("planOperado", operado != null ? operado.getNombre():"");
            
            Instrumentotipooperacionplan itop = servicioDiccionario.get(Instrumentotipooperacionplan.class, operacionPlan.getIdinstrumentotipooperacion());
            if (itop != null) {
            	linea.put("idoperacion", itop.getTipooperacionplan().getIden());
            	linea.put("operacion",servicioDiccionario.getTraduccion(Tipooperacionplan.class, itop.getTipooperacionplan().getIden(), idioma));
            	if (itop.getInstrumentoplan() != null)
    		    	nombreInstrumento = servicioDiccionario.getTraduccion(Instrumentoplan.class, itop.getInstrumentoplan().getIden(), idioma);
    		    else 
    		    	nombreInstrumento = "-";
    		    
    		    linea.put("instrumento", nombreInstrumento);
            } else {
            	linea.put("operacion","-");
            	linea.put("instrumento", "-");
            }
            
            columnlist.add(linea);
		}
		
		data.put("data",columnlist);
		data.put("pages", 1);
		data.put("total", columnlist.size());
		
		return data;
	}

}
