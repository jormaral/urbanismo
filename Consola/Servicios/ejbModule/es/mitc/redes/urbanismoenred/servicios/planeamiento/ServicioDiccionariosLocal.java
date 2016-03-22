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
import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Boletin;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Caracterdeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Grupodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Naturaleza;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Operacioncaracter;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Organo;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioDiccionariosLocal {
	/**
	 * 
	 * @param clase
	 * @param identificador
	 * @throws ExcepcionDiccionario
	 */
	void borrar(Class<?> clase, Object identificador) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Boletin crearBoletin(String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param minimoAplicaciones
	 * @param maximoAplicaciones
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Caracterdeterminacion crearCaracterDeterminacion(String nombre, Integer minimoAplicaciones, Integer maximoAplicaciones, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param correo
	 * @param password
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Centroproduccion crearCentroProduccion(String nombre, String correo, String password, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Grupodocumento crearGrupoDocumento(String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param nemo
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Instrumentoplan crearInstrumentoPlan(String nombre, String nemo, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param tipoOperacion
	 * @param instrumento
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Instrumentotipooperacionplan crearInstrumentoTipoOperacionPlan(int tipoOperacion, int instrumento) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Naturaleza crearNaturaleza(String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param caracterOperado
	 * @param caracterOperador
	 * @param tipoOperacion
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Operacioncaracter crearOperacionCaracter(int caracterOperado, int caracterOperador, Integer tipoOperacion) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Organo crearOrgano(String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Tipodocumento crearTipodocumento(String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Tipooperacionplan crearTipoOperacionPlan(String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Tipotramite crearTipoTramite(String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param clase
	 * @return
	 */
	<T> T[] get(Class<T> clase);
	/**
	 * 
	 * @param clase
	 * @param id
	 * @return
	 */
	<T> T get(Class<T> clase, Object id);
	/**
	 * 
	 * @param idPlan
	 * @return
	 */
	Instrumentoplan getInstrumento(int idPlan);
	
	/**
	 * 
	 * @param idTipoOperacion
	 * @param idInstrumento
	 * @return
	 */
	Instrumentotipooperacionplan getInstrumentoTipoOperacion(int idTipoOperacion, int idInstrumento);
	
	/**
	 * 
	 * @param idInstrumentoPlan
	 * @return
	 */
	Tipooperacionplan[] getOperaciones(int idInstrumentoPlan);
	/**
	 * 
	 * @param idAmbito
	 * @return
	 */
	String[] getPoligonosAmbito(int idAmbito);
	/**
	 * 
	 * @param clase
	 * @param identificador
	 * @param idioma
	 * @return
	 */
	String getTraduccion(Class<?> clase, Object identificador, String idioma);
	/**
	 * 
	 * @param identificador
	 * @param nombre
	 * @param codigo
	 * @param correo
	 * @param password
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Centroproduccion modificarCentroProduccion(int identificador, String nombre, String codigo, String correo, String password, String comentario, String idioma) throws ExcepcionDiccionario;
	/**
	 * 
	 * @param identificador
	 * @param nombre
	 * @param comentario
	 * @param idioma
	 * @return
	 * @throws ExcepcionDiccionario
	 */
	Tipotramite modificarTipoTramite(int identificador, String nombre, String comentario, String idioma) throws ExcepcionDiccionario;
}
