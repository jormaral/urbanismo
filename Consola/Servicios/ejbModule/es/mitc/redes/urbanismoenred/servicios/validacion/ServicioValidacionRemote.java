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

import javax.ejb.Remote;

@Remote
public interface ServicioValidacionRemote {
	/**
	 * Finaliza el proceso de validación para un FIP identificado por el código 
	 * de su trámite. 
	 * Sólo puede haber un proceso de validación inicado para un trámite.
	 * Se debe llamar a este método cuando no se deseen realizar más 
	 * validaciones. 
	 * 
	 * @param idFip Código del trámite del FIP.
	 * 
	 * @throws ExcepcionValidacion Si no se puede finalizar el proceso de validación.
	 */
	public void finalizar(String idFip) throws ExcepcionValidacion;
	
	/**
	 * Inicia el proceso de validación para un FIP identificado por el código 
	 * de su trámite. 
	 * Sólo puede haber un proceso de validación inicado para un trámite.
	 * 
	 * Se debe llamar a este método antes de invocar a cualquier otro.
	 * 
	 * @param idFip Código del trámite del FIP.
	 * @return Identificador del proceso de validación iniciado.
	 * 
	 * @throws ExcepcionValidacion Si no se puede iniciar el proceso de validación.
	 */
	public int iniciar(String idFip) throws ExcepcionValidacion;
	
	/**
	 * Servicio que valida la integridad de un fip que se le pasa como parametro.
	 * Este servicio se encargara de persistir los resultados de la validacion de la integridad
	 * Devolvera true si no ha habido ningun problema en la validacion y se han persistido bien los datos y false en caso contrario
	 * @param idFip
	 * @return
	 */
	public boolean validaIntegridad (String idFip,String nombreUsuario);
	
	/**
	 * Servicio que valida el tramite de un fip que se le pasa como parametro.
	 * Este servicio se encargara de persistir los resultados de la validacion de tramite
	 * Devolvera true si no ha habido ningun problema en la validacion y se han persistido bien los datos y false en caso contrario
	 * @param idFip
	 * @return
	 */
	public boolean validaTramite (String idFip);
	
	
	/**
	 * Servicio que valida las entidades de un fip que se le pasa como parametro.
	 * Este servicio se encargara de persistir los resultados de la validacion de los entidades
	 * Devolvera true si no ha habido ningun problema en la validacion y se han persistido bien los datos y false en caso contrario
	 * @param idFip
	 * @return
	 */
	public boolean validaEntidades (String idFip);
	
	
	/**
	 * Servicio que valida las determinaciones de un fip que se le pasa como parametro.
	 * Este servicio se encargara de persistir los resultados de la validacion de las determinaciones
	 * Devolvera true si no ha habido ningun problema en la validacion y se han persistido bien los datos y false en caso contrario
	 * @param idFip
	 * @return
	 */
	public boolean validaDeterminaciones (String idFip);

    /**
	 * Servicio que valida las CondicionesUrbanisticas de un fip que se le pasa como parametro.
	 * Este servicio se encargara de persistir los resultados de la validacion de las CondicionesUrbanisticas
	 * Devolvera true si no ha habido ningun problema en la validacion y se han persistido bien los datos y false en caso contrario
	 * @param idFip
	 * @return
	 */
	public boolean validaCondicionesUrbanisticas (String idFip);

    /**
	 * Servicio que valida otras validaciones de un fip que se le pasa como parametro.
	 * Este servicio se encargara de persistir los resultados de las otras validaciones
	 * Devolvera true si no ha habido ningun problema en la validacion y se han persistido bien los datos y false en caso contrario
	 * @param idFip
	 * @return
	 */
	public boolean validaOtras (String idFip);
	
	public void validar(ContextoValidacion contexto) throws ExcepcionValidacion;
}
